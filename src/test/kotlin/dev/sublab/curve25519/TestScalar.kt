/**
 *
 * Copyright 2023 SUBSTRATE LABORATORY LLC <info@sublab.dev>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package dev.sublab.curve25519

import dev.sublab.curve25519.scalar.Scalar
import dev.sublab.curve25519.scalar.Scalar.Companion.ONE
import dev.sublab.curve25519.scalar.Scalar.Companion.ZERO
import dev.sublab.curve25519.scalar.functions.*
import dev.sublab.curve25519.support.*
import dev.sublab.hex.hex
import org.junit.jupiter.api.assertThrows
import kotlin.math.pow
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class TestScalar {
    @Test
    fun packageConstructorDoesNotThrowOnValid() {
        val s = ByteArray(32)
        s[31] = 0x7f
        Scalar(s)
    }

    @Test
    fun packageConstructorThrowsOnHighBitSet() {
        val s = ByteArray(32)
        s[31] = 0x80.toByte()

        assertThrows<IllegalArgumentException> {
            Scalar(s)
        }
    }

    @Test
    fun packageConstructorThrowsOnTooShort() {
        assertThrows<IllegalArgumentException> {
            Scalar(ByteArray(31))
        }
    }

    @Test
    fun packageConstructorThrowsOnTooLong() {
        assertThrows<IllegalArgumentException> {
            Scalar(ByteArray(33))
        }
    }

    @Test
    fun packageConstructorPreventsMutability() {
        // Create byte array representing a zero scalar
        val bytes = ByteArray(32)

        // Create a scalar from bytes
        val s = Scalar(bytes)
        assertEquals(ZERO, s)

        // Modify the bytes
        bytes[0] = 1

        // The scalar should be unaltered
        assertEquals(ZERO, s)
    }

    @Test
    fun toByteArrayPreventsMutability() {
        // Create a zero scalar
        val s = Scalar(ByteArray(32))
        assertEquals(ZERO, s)

        // Grab the scalar as bytes
        val bytes = s.toByteArray()

        // Modify the bytes
        bytes[0] = 1

        // The scalar should be unaltered
        assertEquals(ZERO, s)
    }

    @Test
    fun reduce() {
        val biggest = "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"
            .hex.decode()
            .toScalarBytesModOrder()
        assertEquals(CANONICAL_2_256_MINUS_1, biggest)
    }

    @Test
    fun reduceWide() {
        val biggest = "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff0000000000000000000000000000000000000000000000000000000000000000"
            .hex.decode()
            .toScalarBytesModOrderWide()
        assertEquals(CANONICAL_2_256_MINUS_1, biggest)
    }

    @Test
    fun canonicalDecoding() {
        // Canonical encoding of 1667457891
        val canonicalBytes = "6363636300000000000000000000000000000000000000000000000000000000".hex.decode()
        canonicalBytes.toScalarCanonicalBytes()
    }

    @Test
    fun nonCanonicalDecodingUnreduced() {
        // Encoding of
        // 7265385991361016183439748078976496179028704920197054998554201349516117938192
        // = 28380414028753969466561515933501938171588560817147392552250411230663687203
        // (mod l)
        // Non-canonical because unreduced mod l
        val nonCanonicalBytesBecauseUnreduced = ByteArray(32)
        for (i in 0..31) {
            nonCanonicalBytesBecauseUnreduced[i] = 16
        }

        assertThrows<IllegalArgumentException> {
            nonCanonicalBytesBecauseUnreduced.toScalarCanonicalBytes()
        }
    }

    @Test
    fun nonCanonicalDecodingHighbit() {
        // Encoding with high bit set, to check that the parser isn't pre-masking the
        // high bit
        val nonCanonicalBytesBecauseHighbit = ByteArray(32)
        nonCanonicalBytesBecauseHighbit[31] = 0x80.toByte()

        assertThrows<IllegalArgumentException> {
            nonCanonicalBytesBecauseHighbit.toScalarCanonicalBytes()
        }
    }

    @Test
    fun fromBitsClearsHighbit() {
        val exact = "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"
            .hex.decode()
            .toScalarFromBits()
        assertContentEquals(
            "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff7f".hex.decode(),
            exact.toByteArray())
    }

    @Test
    fun addDoesNotReduceNonCanonical() {
        val largestEd25519Scalar = "f8ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff7f"
            .hex.decode()
            .toScalarFromBits()

        val result = "7e344775474a7f9723b63a8be92ae76dffffffffffffffffffffffffffffff0f"
            .hex.decode()
            .toScalarCanonicalBytes()

        assertNotEquals(result, largestEd25519Scalar.add(ONE))
        assertEquals(result, largestEd25519Scalar.add(ONE).reduce())
    }

    @Test
    fun subtractDoesNotReduceNonCanonical() {
        val largestEd25519Scalar = "f8ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff7f"
            .hex.decode()
            .toScalarFromBits()

        val result = "7c344775474a7f9723b63a8be92ae76dffffffffffffffffffffffffffffff0f"
            .hex.decode()
            .toScalarCanonicalBytes()

        assertNotEquals(result, largestEd25519Scalar.subtract(ONE))
        assertEquals(result, largestEd25519Scalar.subtract(ONE).reduce())
    }

    @Test
    fun multiply() {
        assertEquals(X_TIMES_Y, X.multiply(Y))
        assertEquals(Y, X_TIMES_Y.multiply(XINV))
    }

    @Test
    fun nonAdjacentForm() {
        val naf = A_SCALAR.nonAdjacentForm()
        assertContentEquals(A_NAF, naf)
    }

    @Test
    fun testVectorFromBytesModOrderWide() {
        assertEquals(Scalar(TV1_R), TV1_R_INPUT.toScalarBytesModOrderWide())
    }

    @Test
    fun testVectorMultiplyAndAdd() {
        val h = Scalar(TV1_H)
        val a = Scalar(TV1_A)
        val r = Scalar(TV1_R)
        val s = Scalar(TV1_S)

        assertEquals(s, h.multiplyAndAdd(a, r))
        assertEquals(s, h.multiply(a).add(r))
        assertEquals(s.subtract(r), h.multiply(a))
    }

    @Test
    fun fromBytesModOrderWideThrowsOnTooShort() {
        assertThrows<IllegalArgumentException> {
            ByteArray(63).toScalarBytesModOrderWide()
        }
    }

    @Test
    fun fromBytesModOrderWideThrowsOnTooLong() {
        assertThrows<IllegalArgumentException> {
            ByteArray(65).toScalarBytesModOrderWide()
        }
    }

    /**
     * Test method for [Scalar.toRadix16].
     */
    @Test
    fun testToRadix16() {
//        assertContentEquals(RADIX16_ZERO, ZERO.toRadix16())
//        assertContentEquals(RADIX16_ONE, ONE.toRadix16())
        assertContentEquals(RADIX16_42, FORTYTWO.toRadix16())

        val from1234567890 = S1234567890.toRadix16()
        var total = 0
        for (i in from1234567890.indices) {
            assert(from1234567890[i] >= (-8).toByte())
            assert(from1234567890[i] <= 7.toByte())
            total += (from1234567890[i] * 16.0.pow(i.toDouble())).toInt()
        }

        assertEquals(1234567890, total)
        val tv1HR16 = Scalar(TV1_H).toRadix16()
        for (i in tv1HR16.indices) {
            assert(tv1HR16[i] >= (-8).toByte())
            assert(tv1HR16[i] <= 7.toByte())
        }
    }
}