package io.mattmoore.kotlin.playground

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlin.experimental.and

class ByteOperationsSpec : StringSpec({
    "bit operations in Kotlin" {
        fun getUnsignedIntLE(b: ByteArray): Long {
            val first = (b[3] and 0xff.toByte()).toLong() shl 24
            val second = (b[2] and 0xff.toByte()).toLong() shl 16
            val third = (b[1] and 0xff.toByte()).toLong() shl 8
            val fourth = (b[0] and 0xff.toByte()).toLong()
            return first or second or third or fourth

            // Really long line version of this
            // return ((b[3] and 0xff.toByte()).toLong() shl 24) or ((b[2] and 0xff.toByte()).toLong() shl 16) or ((b[1] and 0xff.toByte()).toLong() shl 8) or (b[0] and 0xff.toByte()).toLong()
        }

        val input = listOf(20, 10, 30, 5)
                .map { it.toByte() }
                .toByteArray()
        val result = getUnsignedIntLE(input)
        result shouldBe 85854740
    }
})