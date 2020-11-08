package io.mattmoore.kotlin.playground

import arrow.fx.IO
import arrow.fx.extensions.fx
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.internal.matchers.Any

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
open class RetailerDao {
  open fun totalSalesAmount(retailerId: Int) =
    IO.just(2000.0)
}

/**
 * PURE
 * Purpose is to accept a set of values collected from:
 *   1. Inbound controller request of the retailer ID to check.
 *   2. Total sales amount from DB for that retailer ID.
 *
 * Given the above values, determine whether the retailer qualifies for an alert, but do not actually send the alert.
 */
open class SalesChecker {
  val alertRules = listOf(
    { amount: Double -> amount >= 1000.0 }
  )

  open fun shouldAlert(salesAmount: Double): Boolean =
    alertRules.all { it(salesAmount) }
}

open class RetailerService(
  private val retailerDao: RetailerDao,
  private val alerterService: AlerterService
) {
  open fun fetchAndCheckAlerts(retailerId: Int) = IO.fx {
    val amount = !retailerDao.totalSalesAmount(retailerId)
    if (SalesChecker().shouldAlert(amount)) {
      !alerterService.alert("Retailer $retailerId had $amount of sales")
    }
    amount
  }
}

class AlerterServiceSpec : DescribeSpec({
  describe("functional approach to fetch and verify") {
    describe("when the amount is below 1000.0") {
      val mockRetailerDao = mock(RetailerDao::class.java)
      val mockAlerterService = mock(AlerterService::class.java)
      `when`(mockRetailerDao.totalSalesAmount(1)).thenReturn(IO.just(999.0))
      val result = RetailerService(mockRetailerDao, mockAlerterService).fetchAndCheckAlerts(1).unsafeRunSync()

      it("returns the total sales amount") {
        result shouldBe 999.0
        verifyNoInteractions(mockAlerterService)
      }
    }

    describe("when the amount is above 1000.0") {
      val mockRetailerDao = mock(RetailerDao::class.java)
      val mockAlerterService = mock(AlerterService::class.java)
      `when`(mockRetailerDao.totalSalesAmount(1)).thenReturn(IO.just(2000.0))
      val result = RetailerService(mockRetailerDao, mockAlerterService).fetchAndCheckAlerts(1).unsafeRunSync()

      it("returns the total sales amount") {
        result shouldBe 2000.0
        verify(mockAlerterService, times(1)).alert("Retailer 1 had 2000 of sales")
      }
    }
  }
})