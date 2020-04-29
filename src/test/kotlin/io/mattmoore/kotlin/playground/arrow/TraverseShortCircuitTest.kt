package io.mattmoore.kotlin.playground.arrow

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

import arrow.core.*
import arrow.core.extensions.either.applicative.applicative
import arrow.core.extensions.list.traverse.traverse

import java.io.File

sealed class AppError {
    data class FileFailed(val file: String) : AppError()
}

class TraverseShortCircuitTest : StringSpec({
    fun readFile(file: String): Either<AppError.FileFailed, String> =
            try {
                Right(File(file).readText())
            } catch (e: Exception) {
                Left(AppError.FileFailed(e.localizedMessage))
            }

    // This function loads all the files, but short-circuits when one of them fails.
    // Traverse applies an effectful function to a list of pure values.
    // This is different than sequence, which processes a list of effects.
    fun loadShortCircuit(files: List<String>) =
            files.traverse(Either.applicative(), ::readFile)

    "traverse will apply an effectful function across the collection, for example, List<A>" {
        val filePaths = listOf(
                "data/part1.txt",
                "data/part2.txt",
                "data/part3.txt",
                "data/part4.txt"
        )

        loadShortCircuit(filePaths) shouldBe Right(listOf(
                "I'm the first part.\n",
                "I'm the second part.\n",
                "I'm the third part.\n",
                "I'm the fourth part.\n"
        ))
    }

    "let's try the same thing, but see what happens when an error occurs" {
        val filePaths = listOf(
                "data/part1.txt",
                "data/monkeywrench.txt",
                "data/part3.txt",
                "data/part4.txt"
        )

        val expected = Left(
                AppError.FileFailed(
                        "data/monkeywrench.txt (No such file or directory)"
                )
        )

        val result = loadShortCircuit(filePaths)

        result shouldBe expected
    }
})
