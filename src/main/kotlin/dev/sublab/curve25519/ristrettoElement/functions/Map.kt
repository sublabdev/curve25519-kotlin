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

package dev.sublab.curve25519.ristrettoElement.functions

import dev.sublab.curve25519.edwardsPoint.EdwardsPoint
import dev.sublab.curve25519.fieldElement.*
import dev.sublab.curve25519.fieldElement.FieldElement.Companion.EDWARDS_D
import dev.sublab.curve25519.fieldElement.FieldElement.Companion.D_MINUS_ONE_SQ
import dev.sublab.curve25519.fieldElement.FieldElement.Companion.MINUS_ONE
import dev.sublab.curve25519.fieldElement.FieldElement.Companion.ONE
import dev.sublab.curve25519.fieldElement.FieldElement.Companion.ONE_MINUS_D_SQ
import dev.sublab.curve25519.fieldElement.FieldElement.Companion.SQRT_AD_MINUS_ONE
import dev.sublab.curve25519.fieldElement.FieldElement.Companion.SQRT_M1
import dev.sublab.curve25519.fieldElement.functions.*
import dev.sublab.curve25519.ristrettoElement.RistrettoElement

/**
 * The function MAP(t) from section 3.2.4 of the ristretto255 ID.
 */
internal fun FieldElement.map(): RistrettoElement {
    val r = square().multiply(SQRT_M1)
    val u = r.add(ONE).multiply(ONE_MINUS_D_SQ)
    val v = MINUS_ONE.subtract(r.multiply(EDWARDS_D)).multiply(r.add(EDWARDS_D))
    val sqrt = sqrtRatioM1(u, v)
    var s = sqrt.result
    val sPrime = s.multiply(this).ctAbs().negate()
    s = sPrime.ctSelect(s, sqrt.wasSquare)
    val c = r.ctSelect(MINUS_ONE, sqrt.wasSquare)
    val n =
        c.multiply(r.subtract(ONE)).multiply(D_MINUS_ONE_SQ).subtract(v)
    val sSq = s.square()
    val w0 = s.add(s).multiply(v)
    val w1 = n.multiply(SQRT_AD_MINUS_ONE)
    val w2 = ONE.subtract(sSq)
    val w3 = ONE.add(sSq)

    return RistrettoElement(
        EdwardsPoint(w0.multiply(w3), w2.multiply(w1), w1.multiply(w3), w0.multiply(w2))
    )
}