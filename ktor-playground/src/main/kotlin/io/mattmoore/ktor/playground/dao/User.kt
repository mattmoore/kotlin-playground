package io.mattmoore.ktor.playground.dao

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import arrow.fx.IO
import arrow.fx.handleError
import io.mattmoore.ktor.playground.models.User

private val users = listOf(
  User(1, "Matt", "Moore")
)

class UserDao {
  fun all() = IO<Either<String, List<User>>> {
    Right(users)
  }.handleError { Left(it.message) }

  fun get(id: Int) = IO<Either<String, User>> {
    Right(users.find { it.id == id }!!)
  }.handleError { Left(it.message) }
}