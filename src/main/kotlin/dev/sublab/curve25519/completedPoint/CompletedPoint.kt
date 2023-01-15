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

package dev.sublab.curve25519.completedPoint

import dev.sublab.curve25519.edwardsPoint.EdwardsPoint
import dev.sublab.curve25519.fieldElement.FieldElement
import dev.sublab.curve25519.fieldElement.functions.multiply
import dev.sublab.curve25519.projectivePoint.ProjectivePoint

/**
 * A point $((X:Z), (Y:T))$ on the $\mathbb P^1 \times \mathbb P^1$ model of the
 * curve.
 */
internal class CompletedPoint(
    val x: FieldElement,
    val y: FieldElement,
    val z: FieldElement,
    val t: FieldElement
) {
    /**
     * Convert this point from the $\mathbb P^1 \times \mathbb P^1$ model to the
     * $\mathbb P^2$ model.
     *
     *
     * This costs $3 \mathrm M$.
     */
    fun toProjective() = ProjectivePoint(x.multiply(t), y.multiply(z), z.multiply(t))

    /**
     * Convert this point from the $\mathbb P^1 \times \mathbb P^1$ model to the
     * $\mathbb P^3$ model.
     *
     *
     * This costs $4 \mathrm M$.
     */
    fun toExtended() = EdwardsPoint(x.multiply(t), y.multiply(z), z.multiply(t), x.multiply(y))
}