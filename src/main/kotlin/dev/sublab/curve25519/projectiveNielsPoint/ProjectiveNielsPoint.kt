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

package dev.sublab.curve25519.projectiveNielsPoint

import dev.sublab.curve25519.fieldElement.FieldElement

/**
 * A pre-computed point on the $\mathbb P^3$ model of the curve, represented as
 * $(Y+X, Y-X, Z, 2dXY)$ in "Niels coordinates".
 */
internal class ProjectiveNielsPoint(
    val yPlusX: FieldElement,
    val yMinusX: FieldElement,
    val z: FieldElement,
    val t2D: FieldElement
) {
    /**
     * Constant-time selection between two ProjectiveNielsPoints.
     *
     * @param that the other point.
     * @param b    must be 0 or 1, otherwise results are undefined.
     * @return a copy of this if $b == 0$, or a copy of that if $b == 1$.
     */
    fun ctSelect(that: ProjectiveNielsPoint, b: Int) = ProjectiveNielsPoint(
        yPlusX.ctSelect(that.yPlusX, b),
        yMinusX.ctSelect(that.yMinusX, b),
        z.ctSelect(that.z, b),
        t2D.ctSelect(that.t2D, b)
    )

    /**
     * Point negation.
     *
     * @return $-P$
     */
    fun negate() = ProjectiveNielsPoint(yMinusX, yPlusX, z, t2D.negate())

    companion object {
        val IDENTITY = ProjectiveNielsPoint(
            FieldElement.ONE,
            FieldElement.ONE,
            FieldElement.ONE,
            FieldElement.ZERO
        )
    }
}