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

package dev.sublab.curve25519.affineNielsPoint

import dev.sublab.curve25519.affineNielsPoint.functions.buildNafLookupTable
import dev.sublab.curve25519.edwardsPoint.EdwardsPoint
import dev.sublab.curve25519.fieldElement.FieldElement

/**
 * A pre-computed point on the affine model of the curve, represented as $(y+x,
 * y-x, 2dxy)$ in "Niels coordinates".
 */
internal class AffineNielsPoint(
    val yPlusX: FieldElement,
    val yMinusX: FieldElement,
    val xy2D: FieldElement
) {
    /**
     * Constant-time selection between two AffineNielsPoints.
     *
     * @param that the other point.
     * @param b    must be 0 or 1, otherwise results are undefined.
     * @return a copy of this if $b == 0$, or a copy of that if $b == 1$.
     */
    fun ctSelect(that: AffineNielsPoint, b: Int) = AffineNielsPoint(
        yPlusX.ctSelect(that.yPlusX, b),
        yMinusX.ctSelect(that.yMinusX, b),
        xy2D.ctSelect(that.xy2D, b)
    )

    /**
     * Point negation.
     *
     * @return $-P$
     */
    fun negate() = AffineNielsPoint(yMinusX, yPlusX, xy2D.negate())

    companion object {
        val IDENTITY = AffineNielsPoint(
            FieldElement.ONE,
            FieldElement.ONE,
            FieldElement.ZERO
        )

        /**
         * Odd multiples of the Ed25519 basepoint.
         */
        val AFFINE_ODD_MULTIPLES_OF_BASEPOINT = AffineNielsPoint.buildNafLookupTable(
            EdwardsPoint.ED25519_BASEPOINT
        )
    }
}