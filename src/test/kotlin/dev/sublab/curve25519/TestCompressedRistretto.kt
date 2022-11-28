package dev.sublab.curve25519

import dev.sublab.curve25519.edwardsPoint.CompressedEdwardsY
import dev.sublab.curve25519.ristrettoElement.CompressedRistretto
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFalse

internal class TestCompressedRistretto {
    @Test
    fun constructorDoesNotThrowOnCorrectLength() {
        CompressedRistretto(ByteArray(32))
    }

    @Test
    fun constructorThrowsOnTooShort() {
        assertThrows<IllegalArgumentException> {
            CompressedRistretto(ByteArray(31))
        }
    }

    @Test
    fun constructorThrowsOnTooLong() {
        assertThrows<IllegalArgumentException> {
            CompressedRistretto(ByteArray(33))
        }
    }

    @Test
    fun toByteArray() {
        val s = ByteArray(32)
        s[0] = 0x1f
        assertContentEquals(s, CompressedRistretto(s).toByteArray())
    }

    @Test
    fun equalityRequiresSameClass() {
        val s = ByteArray(32)
        val r = CompressedRistretto(s)
        val e = CompressedEdwardsY(s)
        assertFalse(r.equals(s))
    }
}