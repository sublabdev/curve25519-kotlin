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

package dev.sublab.curve25519.projectivePoint

import dev.sublab.curve25519.completedPoint.CompletedPoint
import dev.sublab.curve25519.edwardsPoint.EdwardsPoint
import dev.sublab.curve25519.fieldElement.FieldElement
import dev.sublab.curve25519.fieldElement.functions.multiply
import dev.sublab.curve25519.fieldElement.functions.square
import dev.sublab.curve25519.fieldElement.functions.squareAndDouble

/**
 * A point $(X:Y:Z)$ on the $\mathbb P^2$ model of the curve.
 */
internal class ProjectivePoint(
    val x: FieldElement,
    val y: FieldElement,
    val z: FieldElement
) {
    /**
     * Convert this point from the $\mathbb P^2$ model to the $\mathbb P^3$ model.
     *
     *
     * This costs $3 \mathrm M + 1 \mathrm S$.
     */
    fun toExtended() = EdwardsPoint(x.multiply(z), y.multiply(z), z.square(), x.multiply(y))

    /**
     * Point doubling: add this point to itself.
     *
     * @return $[2]P$ as a CompletedPoint.
     */
    fun double(): CompletedPoint {
        val xx = x.square()
        val yy = y.square()
        val zz2 = z.squareAndDouble()
        val xPlusY = x.add(y)
        val xPlusYSq = xPlusY.square()
        val yyPlusXx = yy.add(xx)
        val yyMinusXx = yy.subtract(xx)

        return CompletedPoint(xPlusYSq.subtract(yyPlusXx), yyPlusXx, yyMinusXx, zz2.subtract(yyMinusXx))
    }
}