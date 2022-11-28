package dev.sublab.curve25519

import dev.sublab.curve25519.edwardsPoint.CompressedEdwardsY
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertContentEquals

internal class TestCompressedEdwardsY {
    @Test
    fun constructorDoesNotThrowOnCorrectLength() {
        assertDoesNotThrow {
            CompressedEdwardsY(ByteArray(32))
        }
    }

    @Test
    fun constructorThrowsOnTooShort() {
        assertThrows<IllegalArgumentException> {
            CompressedEdwardsY(ByteArray(31))
        }
    }

    @Test
    fun constructorThrowsOnTooLong() {
        assertThrows<IllegalArgumentException> {
            CompressedEdwardsY(ByteArray(33))
        }
    }

    @Test
    fun toByteArray() {
        val s = ByteArray(32)
        s[0] = 0x1f

        assertContentEquals(s, CompressedEdwardsY(s).toByteArray())
    }
}