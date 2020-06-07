package io.mattmoore.kotlin.playground.arrow.monads

import arrow.fx.IO
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class IOSpec : DescribeSpec({
  val addresses = listOf(
    Address(1, "123 Anywhere Street", "Chicago", "IL")
  )

  val customers = listOf(
    Customer(1, "Matt", "Moore", 1)
  )

  val orders = listOf(
    Order(1, 1)
  )

  describe("without IO") {
    fun getOrder(id: Int): Order =
      orders.find { it.id == id }!!

    fun getCustomer(id: Int): Customer =
      customers.find { it.id == id }!!

    fun getAddress(id: Int): Address =
      addresses.find { it.id == id }!!

    fun getAddressFromOrder(orderId: Int): Address {
      val order = getOrder(orderId)
      val customer = getCustomer(order.customerId)
      return getAddress(customer.addressId)
    }

    context("with a valid order ID") {
      it("should return an address") {
        getAddressFromOrder(1) shouldBe addresses.first()
      }
    }

    context("with an invalid order ID") {
      it("should fail") {
        shouldThrow<NullPointerException> {
          getAddressFromOrder(-1)
        }
      }
    }
  }

  describe("with IO") {
    fun getOrder(id: Int) = IO<Order> {
      orders.find { it.id == id }!!
    }

    fun getCustomer(id: Int) = IO<Customer> {
      customers.find { it.id == id }!!
    }

    fun getAddress(id: Int) = IO<Address> {
      addresses.find { it.id == id }!!
    }

    fun getAddressFromOrder(orderId: Int): IO<Address> =
      getOrder(orderId).flatMap { order ->
        getCustomer(order.customerId).flatMap { customer ->
          getAddress(customer.addressId)
        }
      }

    context("with a valid order ID") {
      it("returns an address") {
        getAddressFromOrder(1).suspended() shouldBe addresses.first()
      }
    }

    /**
     * This will still throw an exception if nothing is found.
     * To see how to handle exceptions in a safe way, @see [IOOptionSpec]
     */
    context("with an invalid order ID") {
      it("throws an exception") {
        shouldThrow<java.lang.NullPointerException> {
          getAddressFromOrder(-1).suspended() shouldBe addresses.first()
        }
      }
    }
  }
})