package io.mattmoore.kotlin.playground.arrow.monads

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import arrow.fx.IO
import arrow.fx.handleError
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class IOEitherSpec : DescribeSpec({
  val addresses = listOf(
    Address(1, "123 Anywhere Street", "Chicago", "IL")
  )

  val customers = listOf(
    Customer(1, "Matt", "Moore", 1)
  )

  val orders = listOf(
    Order(1, 1)
  )

  describe("IO with Either") {
    fun getOrder(id: Int): IO<Order> = IO {
      orders.find { it.id == id }!!
    }

    fun getCustomer(id: Int): IO<Customer> = IO {
      customers.find { it.id == id }!!
    }

    fun getAddress(id: Int): IO<Address> = IO {
      addresses.find { it.id == id }!!
    }

    fun getAddressFromOrder(orderId: Int): IO<Either<String, Address>> =
      getOrder(orderId).flatMap { order ->
        getCustomer(order.customerId).flatMap { customer ->
          getAddress(customer.addressId).map { Right(it) }
        }
      }.handleError { Left("Unable to find address.") }

    context("with a valid order ID") {
      it("returns an address") {
        getAddressFromOrder(1).suspended() shouldBe Right(addresses.first())
      }
    }

    /**
     * Unlike in @see [IOSpec], if an address isn't found, we don't throw an exception.
     * Instead, this will return Left type with error message.
     */
    context("with an invalid order ID") {
      it("returns None") {
        getAddressFromOrder(-1).suspended() shouldBe Left("Unable to find address.")
      }
    }
  }
})