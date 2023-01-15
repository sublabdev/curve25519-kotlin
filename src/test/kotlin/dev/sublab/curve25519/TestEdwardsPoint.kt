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

import dev.sublab.curve25519.edwardsPoint.CompressedEdwardsY
import dev.sublab.curve25519.edwardsPoint.EdwardsPoint.Companion.ED25519_BASEPOINT
import dev.sublab.curve25519.edwardsPoint.EdwardsPoint.Companion.IDENTITY
import dev.sublab.curve25519.edwardsPoint.functions.vartimeDoubleScalarMultiplyBasepoint
import dev.sublab.curve25519.scalar.Scalar
import dev.sublab.curve25519.scalar.Scalar.Companion.ONE
import dev.sublab.curve25519.scalar.Scalar.Companion.ZERO
import dev.sublab.curve25519.support.*
import dev.sublab.hex.hex
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TestEdwardsPoint {
    @Test
    fun basepointDecompressionCompression() {
        val b = ED25519_BASEPOINT_COMPRESSED.decompress()
        assertEquals(ED25519_BASEPOINT_COMPRESSED, b.compress())
    }

    @Test
    fun decompressionSignHandling() {
        // Manually set the high bit of the last byte to flip the sign
        val minusBasepointBytes = ED25519_BASEPOINT_COMPRESSED.toByteArray()
        minusBasepointBytes[31] = (minusBasepointBytes[31].toInt() or (1 shl 7)).toByte()
        val minusB = CompressedEdwardsY(minusBasepointBytes).decompress()
        // Test projective coordinates exactly since we know they should
        // only differ by a flipped sign.
        assertEquals(ED25519_BASEPOINT.x.negate(), minusB.x)
        assertEquals(ED25519_BASEPOINT.y, minusB.y)
        assertEquals(ED25519_BASEPOINT.z, minusB.z)
        assertEquals(ED25519_BASEPOINT.t.negate(), minusB.t)
    }

    @Test
    fun ctSelectReturnsCorrectResult() {
        assertEquals(ED25519_BASEPOINT, ED25519_BASEPOINT.ctSelect(IDENTITY, 0))
        assertEquals(IDENTITY, ED25519_BASEPOINT.ctSelect(IDENTITY, 1))
        assertEquals(IDENTITY, IDENTITY.ctSelect(ED25519_BASEPOINT, 0))
        assertEquals(ED25519_BASEPOINT, IDENTITY.ctSelect(ED25519_BASEPOINT, 1))
    }

    @Test
    fun basepointPlusBasepointVsBasepoint2Constant() {
        assertEquals(BASE2_CMPRSSD, ED25519_BASEPOINT.add(ED25519_BASEPOINT).compress())
    }

    @Test
    fun basepointPlusBasepointProjectiveNielsVsBasepoint2Constant() {
        assertEquals(BASE2_CMPRSSD, ED25519_BASEPOINT.add(ED25519_BASEPOINT.toProjectiveNiels()).toExtended().compress())
    }

    @Test
    fun basepointPlusBasepointAffineNielsVsBasepoint2Constant() {
        assertEquals(BASE2_CMPRSSD, ED25519_BASEPOINT.add(ED25519_BASEPOINT.toAffineNiels()).toExtended().compress())
    }

    @Test
    fun basepointDoubleVsBasepoint2Constant() {
        assertEquals(BASE2_CMPRSSD, ED25519_BASEPOINT.double().compress())
    }

    @Test
    fun basepointDoubleMinusBasepoint() {
        assertEquals(ED25519_BASEPOINT, ED25519_BASEPOINT.double().subtract(ED25519_BASEPOINT))
    }

    @Test
    fun basepointNegateVsZeroMinusBasepoint() {
        assertEquals(ED25519_BASEPOINT.negate(), IDENTITY.subtract(ED25519_BASEPOINT))
    }

    @Test
    fun scalarMulVsEd25519py() {
        assertEquals(A_TIMES_BASEPOINT, ED25519_BASEPOINT.multiply(A_SCALAR).compress())
    }

    @Test
    fun testVartimeDoubleScalarMultiplyBasepoint() {
        // Little-endian
        val zero = ZERO
        val one = ONE
        val two = Scalar("0200000000000000000000000000000000000000000000000000000000000000".hex.decode())
        val a = Scalar("d072f8dd9c07fa7bc8d22a4b325d26301ee9202f6db89aa7c3731529e37e437c".hex.decode())
        val A = CompressedEdwardsY("d4cf8595571830644bd14af416954d09ab7159751ad9e0f7a6cbd92379e71a66".hex.decode()).decompress()
        val B = ED25519_BASEPOINT
        val I = IDENTITY

        // 0 * I + 0 * B = I
        assertEquals(I, vartimeDoubleScalarMultiplyBasepoint(zero, I, zero))
        // 1 * I + 0 * B = I
        assertEquals(I, vartimeDoubleScalarMultiplyBasepoint(one, I, zero))
        // 1 * I + 1 * B = B
        assertEquals(B, vartimeDoubleScalarMultiplyBasepoint(one, I, one))
        // 1 * B + 1 * B = 2 * B
        assertEquals(B.double(), vartimeDoubleScalarMultiplyBasepoint(one, B, one))
        // 1 * B + 2 * B = 3 * B
        assertEquals(B.double().add(B), vartimeDoubleScalarMultiplyBasepoint(one, B, two))
        // 2 * B + 2 * B = 4 * B
        assertEquals(B.double().double(), vartimeDoubleScalarMultiplyBasepoint(two, B, two))

        // 0 * B + a * B = A
        assertEquals(A, vartimeDoubleScalarMultiplyBasepoint(zero, B, a))
        // a * B + 0 * B = A
        assertEquals(A, vartimeDoubleScalarMultiplyBasepoint(a, B, zero))
        // a * B + a * B = 2 * A
        assertEquals(A.double(), vartimeDoubleScalarMultiplyBasepoint(a, B, a))
    }

    @Test
    fun doubleScalarMulBasepointVsEd25519py() {
        assertEquals(
            DOUBLE_SCALAR_MULT_RESULT,
            vartimeDoubleScalarMultiplyBasepoint(A_SCALAR, A_TIMES_BASEPOINT.decompress(), B_SCALAR).compress()
        )
    }

    @Test
    fun basepointMulByPow24VsBasepoint16Constant() {
        assertEquals(BASE16_CMPRSSD.decompress(), ED25519_BASEPOINT.multiplyByPow2(4))
    }

    @Test
    fun isIdentity() {
        assert(IDENTITY.isIdentity)
        assert(!ED25519_BASEPOINT.isIdentity)
    }

    @Test
    fun isSmallOrder() {
        // The basepoint has large prime order
        assert(!ED25519_BASEPOINT.isSmallOrder)
        // EIGHT_TORSION_COMPRESSED has all points of small order.
        for (compressed in EIGHT_TORSION_COMPRESSED) {
            assert(compressed.decompress().isSmallOrder)
        }
    }

    @Test
    fun isTorsionFree() {
        // The basepoint is torsion-free.
        assert(ED25519_BASEPOINT.isTorsionFree)

        // Adding the identity leaves it torsion-free.
        assert(ED25519_BASEPOINT.add(IDENTITY).isTorsionFree)

        // Adding any of the 8-torsion points to it (except the identity) affects the
        // result.
        assertEquals(EIGHT_TORSION_COMPRESSED.first(), IDENTITY.compress())
        for (i in 1 until EIGHT_TORSION_COMPRESSED.size) {
            assert(!ED25519_BASEPOINT.add(EIGHT_TORSION_COMPRESSED[i].decompress()).isTorsionFree)
        }
    }
}