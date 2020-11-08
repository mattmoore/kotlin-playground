package io.mattmoore.kotlin.playground

import arrow.fx.IO
import arrow.fx.extensions.fx
import arrow.fx.handleError

/**
 * IMPURE
 * Some alerter service. It's sole purpose to act as a way to send generic messages.
 * No business logic should be attached to this, other than simply wrapping alert calls.
 */
open class AlerterService {
  open fun alert(message: String) =
    IO { println(message) }
}

/**
 * IMPURE
 * Your typical Data Access Layer. This is purely responsible for loading from the DB.
 * No business logic should be attached to this.
 */
open class RetailerDao(val amountToReturn: Double) {
  open fun totalSalesAmount(retailerId: Int) =
    IO.just(amountToReturn)
}

open class RetailerService(
  private val retailerDao: RetailerDao,
  private val alerterService: AlerterService
) {
  fun fetchAndCheckAlerts(retailerId: Int) = IO.fx {
    // IMPURE
    val amount = !retailerDao.totalSalesAmount(retailerId)
    if (amount >= 1000.0) // PURE LOGIC
      // IMPURE LOGIC
      !alerterService.alert("Retailer $retailerId had $amount of sales")
    amount
  }.handleError { println("It all died a horrible death!") }
}

suspend fun main(args: Array<String>) {
  val retailerDao = RetailerDao(2000.0)
  val alerterService = AlerterService()
  val retailerService = RetailerService(retailerDao, alerterService)

  println(
    retailerService.fetchAndCheckAlerts(1).suspended()
  )
}
