package io.mattmoore.kotlin.playground.arrow

sealed class AppError {
    data class FileFailed(val message: String) : AppError()
}
