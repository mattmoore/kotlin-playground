package io.mattmoore.ktor.playground.server.controllers

import arrow.core.Either
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.mattmoore.ktor.playground.server.userService

fun Route.user() {
  route("/users") {
    get("/") {
      val response = userService.all().attempt().map {
        when (it) {
          is Either.Left -> mapOf("error" to it.a.message)
          is Either.Right -> present(it.b)
        }
      }
      call.respond(response.suspended())
    }

    get("/{id}") {
      val id = Integer.parseInt(call.parameters["id"])
      val response = findUser(id).attempt().map {
        when (it) {
          is Either.Left -> mapOf("error" to it.a.message)
          is Either.Right -> present(it.b)
        }
      }
      call.respond(response.suspended())
    }
  }
}