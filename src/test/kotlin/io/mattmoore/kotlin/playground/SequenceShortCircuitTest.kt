package io.mattmoore.kotlin.playground

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

import arrow.core.*
import arrow.core.extensions.either.applicative.applicative
import arrow.core.extensions.list.traverse.sequence

import java.io.File

class SequenceShortCircuitTest : StringSpec({
    "sequence is useful when you have a List<Either<E, A>> instead of a List<A>. in other words, a list of effects" {
        // We can define a function `readFile` that returns an Either<E, A>
        fun readFile(file: String): Either<Exception, String> =
                try {
                    Right(File(file).readText())
                } catch (e: Exception) {
                    Left(e)
                }

        // We're defining a function to load all the files, but short-circuit when one of them fails.
        // Sequence applies to a list of effects to be run.
        // This is different from traverse, which applies an effectful function to a list of pure values.
        fun loadShortCircuit(files: List<Either<Exception, String>>) =
                files.sequence(Either.applicative())

        val expected = Right(listOf(
                "I'm the first part.\n",
                "I'm the second part.\n",
                "I'm the third part.\n",
                "I'm the fourth part.\n"
        ))

        // The list of effects we're going to "sequence"
        val effectList = listOf(
                readFile("data/part1.txt"),
                readFile("data/part2.txt"),
                readFile("data/part3.txt"),
                readFile("data/part4.txt")
        )

        val result = loadShortCircuit(effectList)

        result shouldBe expected
    }
})
