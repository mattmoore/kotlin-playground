package io.mattmoore.kotlin.plugins.annotations.example

import io.mattmoore.kotlin.plugins.annotations.Greeter

@Greeter
class Greeter {
  val firstName = "Matt"
  val lastName = "Moore"
}

fun main(args: Array<String>) {
  println(GeneratedGreeter().greeting())
}
