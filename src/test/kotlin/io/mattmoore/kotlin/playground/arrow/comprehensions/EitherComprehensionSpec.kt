package io.mattmoore.kotlin.playground.arrow.comprehensions

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import arrow.core.extensions.fx
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.util.*

class EitherComprehensionSpec : DescribeSpec({
  describe("comprehension") {
    describe("with Option") {
      fun compare(foo: Either<String, Date>, bar: Either<String, Date>) = Either.fx<String, Boolean> {
        when {
          foo is Left -> false
          foo is Right && bar is Left -> true
          else -> !foo > !bar
        }
      }

      context("when foo is Left, bar is Left") {
        it("returns false") {
          compare(foo = Left("No date"), bar = Left("No date")) shouldBe Right(false)
        }
      }

      context("when foo is Left, bar is Right") {
        it("returns false") {
          compare(
            foo = Left("No date"),
            bar = Right(Date(2020, 6, 4))
          ) shouldBe Right(false)
        }
      }

      context("when foo is Right, bar is Left") {
        it("should return true") {
          compare(
            foo = Right(Date(2020, 6, 3)),
            bar = Left("No date")
          ) shouldBe Right(true)
        }
      }

      context("when both are Right and foo > bar") {
        it("should return true") {
          compare(
            foo = Right(Date(2020, 6, 4)),
            bar = Right(Date(2020, 6, 3))
          ) shouldBe Right(true)
        }
      }

      context("when both are Right and foo < bar") {
        it("should return false") {
          compare(
            foo = Right(Date(2020, 6, 3)),
            bar = Right(Date(2020, 6, 4))
          ) shouldBe Right(false)
        }
      }
    }
  }
})