package io.mattmoore.ktor.playground.server

import arrow.core.Either
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.jwt.jwt
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.routing.Routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.mattmoore.ktor.playground.models.User
import io.mattmoore.ktor.playground.server.controllers.version
import io.mattmoore.ktor.playground.services.UserService
import java.text.DateFormat

open class SimpleJWT(val secret: String) {
  private val algorithm = Algorithm.HMAC256(secret)
  val verifier = JWT.require(algorithm).build()
  fun sign(name: String): String = JWT.create().withClaim("name", name).sign(algorithm)
}

val simpleJwt = SimpleJWT("secret")
val userService = UserService()

fun main(args: Array<String>) {
  embeddedServer(Netty, port = 8080) {
    install(DefaultHeaders)
    install(ContentNegotiation) {
      gson {
        setDateFormat(DateFormat.LONG)
        setPrettyPrinting()
      }
    }
    install(CORS) {
      method(HttpMethod.Options)
      header(HttpHeaders.XForwardedProto)
      anyHost()
      allowSameOrigin = true
      allowCredentials = true
      allowNonSimpleContentTypes = true
    }
    install(Authentication) {
      jwt {
        verifier(simpleJwt.verifier)
        validate { result ->
          val id = Integer.parseInt(result.payload.getClaim("sub").asString())
          val user = userService.get(id).attempt().map { eitherUser ->
            when(eitherUser) {
              is Either.Right -> eitherUser.b
              is Either.Left -> eitherUser.a.message
            }
          }.suspended() as User
          UserIdPrincipal(user.id.toString())
        }
      }
    }
    install(Routing) {
      version()
    }
  }.start(wait = true)
}
