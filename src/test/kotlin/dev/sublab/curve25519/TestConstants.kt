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
import dev.sublab.curve25519.fieldElement.FieldElement.Companion.EDWARDS_2D
import dev.sublab.curve25519.fieldElement.FieldElement.Companion.EDWARDS_D
import dev.sublab.curve25519.fieldElement.FieldElement.Companion.INVSQRT_A_MINUS_D
import dev.sublab.curve25519.fieldElement.FieldElement.Companion.ONE
import dev.sublab.curve25519.fieldElement.FieldElement.Companion.SQRT_AD_MINUS_ONE
import dev.sublab.curve25519.fieldElement.FieldElement.Companion.SQRT_M1
import dev.sublab.curve25519.fieldElement.functions.invert
import dev.sublab.curve25519.fieldElement.functions.multiply
import dev.sublab.curve25519.fieldElement.functions.square
import dev.sublab.curve25519.fieldElement.functions.toFieldElement
import dev.sublab.hex.hex
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TestConstants {
    @Test
    fun checkEdwardsD() {
        assertEquals(
            EDWARDS_D,
            "a3785913ca4deb75abd841414d0a700098e879777940c78c73fe6f2bee6c0352".hex.decode().toFieldElement()
        )
    }

    @Test
    fun checkEdwards2D() {
        val two = ONE.add(ONE)
        assertEquals(EDWARDS_2D, EDWARDS_D.multiply(two))
    }

    @Test
    fun checkSqrtADMinusOne() {
        assertEquals(EDWARDS_D, SQRT_AD_MINUS_ONE.square().add(ONE).negate())
    }

    @Test
    fun checkInvSqrtAMinusD() {
        assertEquals(
            EDWARDS_D,
            INVSQRT_A_MINUS_D.invert().square().add(ONE).negate()
        )
    }

    @Test
    fun checkSqrtM1() {
        assertEquals(
            SQRT_M1,
            "b0a00e4a271beec478e42fad0618432fa7d7fb3d99004d2b0bdfc14f8024832b".hex.decode().toFieldElement()
        )
    }

    @Test
    fun checkEd25519Basepoint() {
        val encoded = CompressedEdwardsY(
            "5866666666666666666666666666666666666666666666666666666666666666".hex.decode()
        )
        val b = encoded.decompress()

        assertEquals(ED25519_BASEPOINT.x, b.x)
        assertEquals(ED25519_BASEPOINT.y, b.y)
        assertEquals(ED25519_BASEPOINT.z, b.z)
        assertEquals(ED25519_BASEPOINT.t, b.t)
    }
}