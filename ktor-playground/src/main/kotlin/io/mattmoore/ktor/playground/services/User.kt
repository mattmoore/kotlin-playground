package io.mattmoore.ktor.playground.services

import arrow.core.Either
import arrow.fx.IO
import io.mattmoore.ktor.playground.dao.UserDao
import io.mattmoore.ktor.playground.models.User

class UserService {
  fun all(): IO<Either<String, List<User>>> = UserDao().all()

  fun get(id: Int): IO<Either<String, User>> = UserDao().get(id)
}