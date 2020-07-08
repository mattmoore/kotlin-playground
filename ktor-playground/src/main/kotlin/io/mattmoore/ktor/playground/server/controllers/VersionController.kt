package io.mattmoore.ktor.playground.server.controllers

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.version() {
  route("/version") {
    get("/") {
      call.respondText("0.1.0", ContentType.Text.Plain)
    }
  }
}