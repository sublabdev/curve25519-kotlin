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

package dev.sublab.curve25519.fieldElement

import dev.sublab.curve25519.fieldElement.FieldElement.Companion.SQRT_M1
import dev.sublab.curve25519.fieldElement.functions.multiply
import dev.sublab.curve25519.fieldElement.functions.powP58
import dev.sublab.curve25519.fieldElement.functions.square

/**
 * The result of calling [sqrtRatioM1].
 */
internal class SqrtRatioM1Result(val wasSquare: Boolean, val result: FieldElement)

/**
 * Given FieldElements $u$ and $v$, compute either $\sqrt{u / v}$ or $\sqrt{i *
 * u / v}$ in constant time.
 *
 *
 * This function always returns the non-negative square root.
 *
 * @param u the numerator.
 * @param v the denominator.
 * @return
 *
 *  * (true, +$\sqrt{u / v}$) if $v$ is non-zero and $u / v$ is square.
 *  * (true, zero) if $u$ is zero.
 *  * (false, zero) if $v$ is zero and $u$ is non-zero.
 *  * (false, +$\sqrt{i * u / v}$) if $u / v$ is non-square (so $i * u
 * / v$ is square).
 */
internal fun sqrtRatioM1(u: FieldElement, v: FieldElement): SqrtRatioM1Result {
    val v3 = v.square().multiply(v)
    val v7 = v3.square().multiply(v)
    var r = u.multiply(v3).multiply(u.multiply(v7).powP58())
    val check = v.multiply(r.square())
    val uNeg = u.negate()
    val correctSignSqrt = check.ctEquals(u)
    val flippedSignSqrt = check.ctEquals(uNeg)
    val flippedSignSqrtM1 = check.ctEquals(uNeg.multiply(SQRT_M1))
    val rPrime = r.multiply(SQRT_M1)
    r = r.ctSelect(rPrime, flippedSignSqrt or flippedSignSqrtM1)

    // Choose the non-negative square root.
    r = r.ctAbs()
    return SqrtRatioM1Result(correctSignSqrt || flippedSignSqrt, r)
}