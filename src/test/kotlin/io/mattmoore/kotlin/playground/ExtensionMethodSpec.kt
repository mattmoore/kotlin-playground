package io.mattmoore.kotlin.playground

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class ExtensionMethodSpec : DescribeSpec({
  describe("add a method to an existing class without modifying the class source") {
    data class Person(val firstName: String, val lastName: String)

    fun Person.greet() = "Hello, $firstName $lastName!"

    it("now has a greet method") {
      Person("Matt", "Moore").greet() shouldBe "Hello, Matt Moore!"
    }
  }

  describe("extending built-in types") {
    fun Int.square() = this * 2

    it("now has a square method") {
      2.square() shouldBe 4
    }
  }
})