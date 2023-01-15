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

import dev.sublab.curve25519.fieldElement.*
import dev.sublab.curve25519.fieldElement.FieldElement.Companion.ONE
import dev.sublab.curve25519.fieldElement.FieldElement.Companion.SQRT_M1
import dev.sublab.curve25519.fieldElement.FieldElement.Companion.ZERO
import dev.sublab.curve25519.fieldElement.functions.*
import dev.sublab.curve25519.support.*
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class TestFieldElement {
    @Test
    fun testAMulAVsASquaredConstant() {
        val a = A_BYTES.toFieldElement()
        val asq = ASQ_BYTES.toFieldElement()
        assertEquals(asq, a.multiply(a))
    }

    @Test
    fun testASquareVsASquaredConstant() {
        val a = A_BYTES.toFieldElement()
        val asq = ASQ_BYTES.toFieldElement()
        assertEquals(asq, a.square())
    }

    @Test
    fun testASquare2VsASquaredConstant() {
        val a = A_BYTES.toFieldElement()
        val asq = ASQ_BYTES.toFieldElement()
        assertEquals(a.squareAndDouble(), asq.add(asq))
    }

    @Test
    fun testAInvertVsInverseOfAConstant() {
        val a = A_BYTES.toFieldElement()
        val ainv = AINV_BYTES.toFieldElement()
        val aInversed = a.invert()

        assertEquals(ainv, aInversed)
        assertEquals(ONE, a.multiply(aInversed))
    }

    @Test
    fun sqrtRatioM1Behavior() {
        val zero = ZERO
        val one = ONE
        val i = SQRT_M1
        val two = one.add(one) // 2 is nonsquare mod p.
        val four = two.add(two) // 4 is square mod p.

        // 0/0 should return (1, 0) since u is 0
        var sqrt = sqrtRatioM1(zero, zero)
        assert(sqrt.wasSquare)
        assertEquals(zero, sqrt.result)
        assert(!sqrt.result.isNegative)

        // 1/0 should return (0, 0) since v is 0, u is nonzero
        sqrt = sqrtRatioM1(one, zero)
        assert(!sqrt.wasSquare)
        assertEquals(zero, sqrt.result)
        assert(!sqrt.result.isNegative)

        // 2/1 is nonsquare, so we expect (0, sqrt(i*2))
        sqrt = sqrtRatioM1(two, one)
        assert(!sqrt.wasSquare)
        assertEquals(two.multiply(i), sqrt.result.square())
        assert(!sqrt.result.isNegative)

        // 4/1 is square, so we expect (1, sqrt(4))
        sqrt = sqrtRatioM1(four, one)
        assert(sqrt.wasSquare)
        assertEquals(four, sqrt.result.square())
        assert(!sqrt.result.isNegative)

        // 1/4 is square, so we expect (1, 1/sqrt(4))
        sqrt = sqrtRatioM1(one, four)
        assert(sqrt.wasSquare)
        assertEquals(one, sqrt.result.square().multiply(four))
    }

    @Test
    fun testAP58VsAP58Constant() {
        val a = A_BYTES.toFieldElement()
        val a58 = AP58_BYTES.toFieldElement()
        assertEquals(a58, a.powP58())
    }

    @Test
    fun equality() {
        val a = A_BYTES.toFieldElement()
        val ainv = AINV_BYTES.toFieldElement()
        assertEquals(a, a)
        assertNotEquals(a, ainv)
    }

    @Test
    fun fromByteArrayHighbitIsIgnored() {
        val clearedBytes = B_BYTES
        clearedBytes[31] = (clearedBytes[31].toInt() and 127).toByte()

        val withHighbitSet = B_BYTES.toFieldElement()
        val withoutHighbitSet = clearedBytes.toFieldElement()
        assertEquals(withHighbitSet, withoutHighbitSet)
    }

    @Test
    fun encodeAndDecodeOnZero() {
        val zero = ByteArray(32)
        val a = zero.toFieldElement()
        assertEquals(ZERO, a)
        assertContentEquals(zero, a.toByteArray())
    }

    @Test
    fun ctSelectReturnsCorrectResult() {
        val aa = IntArray(10)
        val bb = IntArray(10)

        for (i in 0..9) {
            aa[i] = i
            bb[i] = 10 - i
        }

        val a = FieldElement(aa)
        val b = FieldElement(bb)

        assertEquals(a, a.ctSelect(b, 0))
        assertEquals(b, a.ctSelect(b, 1))
        assertEquals(b, b.ctSelect(a, 0))
        assertEquals(a, b.ctSelect(a, 1))
    }
}