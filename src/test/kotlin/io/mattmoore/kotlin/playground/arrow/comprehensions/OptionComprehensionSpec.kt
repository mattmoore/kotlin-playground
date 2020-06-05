package io.mattmoore.kotlin.playground.arrow.comprehensions

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.extensions.fx
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.util.*

class OptionComprehensionSpec : DescribeSpec({
  describe("comprehension") {
    describe("with Option") {
      fun compare(foo: Option<Date>, bar: Option<Date>) = Option.fx<Boolean> {
        return@fx when {
          foo is None -> false
          foo is Some && bar is None -> true
          else -> !foo > !bar
        }
      }

      context("when foo is None, bar is None") {
        it("returns false") {
          compare(foo = None, bar = None) shouldBe Some(false)
        }
      }

      context("when foo is None, bar is Some") {
        it("returns false") {
          compare(
            foo = None,
            bar = Some(Date(2020, 6, 4))
          ) shouldBe Some(false)
        }
      }

      context("when foo is Some, bar is None") {
        it("should return true") {
          compare(
            foo = Some(Date(2020, 6, 3)),
            bar = None
          ) shouldBe Some(true)
        }
      }

      context("when both are Some, foo > bar") {
        it("should return true") {
          compare(
            foo = Some(Date(2020, 6, 4)),
            bar = Some(Date(2020, 6, 3))
          ) shouldBe Some(true)
        }
      }

      context("when both are Some, foo < bar") {
        it("should return false") {
          compare(
            foo = Some(Date(2020, 6, 3)),
            bar = Some(Date(2020, 6, 4))
          ) shouldBe Some(false)
        }
      }
    }
  }
})