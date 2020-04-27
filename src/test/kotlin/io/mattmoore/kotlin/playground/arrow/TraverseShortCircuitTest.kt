package io.mattmoore.kotlin.playground.arrow

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

import arrow.core.*
import arrow.core.extensions.either.applicative.applicative
import arrow.core.extensions.list.traverse.traverse

import java.io.File

class TraverseShortCircuitTest : StringSpec({
    "traverse will apply an effectful function across the collection, for example, List<A>" {
        // We can define a function `readFile` that returns an Either<E, A>
        fun readFile(file: String): Either<Exception, String> =
                try {
                    Right(File(file).readText())
                } catch (e: Exception) {
                    Left(e)
                }

        // We're defining a function to load all the files, but short-circuit when one of them fails.
        // Traverse applies an effectful function to a list of pure values.
        // This is different than sequence, which processes a list of effects.
        fun loadShortCircuit(files: List<String>) =
                files.traverse(Either.applicative(), ::readFile)

        val expected = Right(listOf(
                "I'm the first part.\n",
                "I'm the second part.\n",
                "I'm the third part.\n",
                "I'm the fourth part.\n"
        ))

        // Instead of passing in a list of effects like we do with sequence, pass a list of pure string values.
        val filePaths = listOf(
                "data/part1.txt",
                "data/part2.txt",
                "data/part3.txt",
                "data/part4.txt"
        )

        val result = loadShortCircuit(filePaths)

        result shouldBe expected
    }
})
