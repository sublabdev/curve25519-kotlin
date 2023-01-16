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

package dev.sublab.curve25519.fieldElement.functions

import dev.sublab.curve25519.fieldElement.FieldElement

/**
 * $h = f * f$
 *
 *
 * Can overlap $h$ with $f$.
 *
 *
 * Preconditions:
 *
 *  * $|f|$ bounded by $1.65*2^{26},1.65*2^{25},1.65*2^{26},1.65*2^{25},$ etc.
 *
 *
 * Postconditions:
 *
 *  * $|h|$ bounded by $1.01*2^{25},1.01*2^{24},1.01*2^{25},1.01*2^{24},$ etc.
 *
 *
 * See [.multiply] for discussion of implementation
 * strategy.
 *
 * @return The (reasonably reduced) square of this field element.
 */
internal fun FieldElement.square(multiplicator: Int = 1): FieldElement {
    val f = t.copyOf()

    // pre-calculated multiplications
    val f0m2 = 2 * f[0]
    val f1m2 = 2 * f[1]
    val f2m2 = 2 * f[2]
    val f3m2 = 2 * f[3]
    val f4m2 = 2 * f[4]
    val f5m2 = 2 * f[5]
    val f6m2 = 2 * f[6]
    val f7m2 = 2 * f[7]
    val f5m38 = 38 * f[5] /* 1.959375*2^30 */
    val f6m19 = 19 * f[6] /* 1.959375*2^30 */
    val f7m38 = 38 * f[7] /* 1.959375*2^30 */
    val f8m19 = 19 * f[8] /* 1.959375*2^30 */
    val f9m38 = 38 * f[9] /* 1.959375*2^30 */

    // more pre-calculations
    val f0f0 = f[0] * f[0].toLong()
    val f0f1m2 = f0m2 * f[1].toLong()
    val f0f2m2 = f0m2 * f[2].toLong()
    val f0f3m2 = f0m2 * f[3].toLong()
    val f0f4m2 = f0m2 * f[4].toLong()
    val f0f5m2 = f0m2 * f[5].toLong()
    val f0f6m2 = f0m2 * f[6].toLong()
    val f0f7m2 = f0m2 * f[7].toLong()
    val f0f8m2 = f0m2 * f[8].toLong()
    val f0f9m2 = f0m2 * f[9].toLong()
    val f1f1m2 = f1m2 * f[1].toLong()
    val f1f2m2 = f1m2 * f[2].toLong()
    val f1f3m4 = f1m2 * f3m2.toLong()
    val f1f4m2 = f1m2 * f[4].toLong()
    val f1f5m4 = f1m2 * f5m2.toLong()
    val f1f6m2 = f1m2 * f[6].toLong()
    val f1f7m4 = f1m2 * f7m2.toLong()
    val f1f8m2 = f1m2 * f[8].toLong()
    val f1f9m76 = f1m2 * f9m38.toLong()
    val f2f2 = f[2] * f[2].toLong()
    val f2f3m2 = f2m2 * f[3].toLong()
    val f2f4m2 = f2m2 * f[4].toLong()
    val f2f5m2 = f2m2 * f[5].toLong()
    val f2f6m2 = f2m2 * f[6].toLong()
    val f2f7m2 = f2m2 * f[7].toLong()
    val f2f8m38 = f2m2 * f8m19.toLong()
    val f2f9m38 = f[2] * f9m38.toLong()
    val f3f3m2 = f3m2 * f[3].toLong()
    val f3f4m2 = f3m2 * f[4].toLong()
    val f3f5m4 = f3m2 * f5m2.toLong()
    val f3f6m2 = f3m2 * f[6].toLong()
    val f3f7m76 = f3m2 * f7m38.toLong()
    val f3f8m38 = f3m2 * f8m19.toLong()
    val f3f9m76 = f3m2 * f9m38.toLong()
    val f4f4 = f[4] * f[4].toLong()
    val f4f5m2 = f4m2 * f[5].toLong()
    val f4f6m38 = f4m2 * f6m19.toLong()
    val f4f7m38 = f[4] * f7m38.toLong()
    val f4f8m38 = f4m2 * f8m19.toLong()
    val f4f9m38 = f[4] * f9m38.toLong()
    val f5f5m38 = f[5] * f5m38.toLong()
    val f5f6m38 = f5m2 * f6m19.toLong()
    val f5f7m76 = f5m2 * f7m38.toLong()
    val f5f8m38 = f5m2 * f8m19.toLong()
    val f5f9m76 = f5m2 * f9m38.toLong()
    val f6f6m19 = f[6] * f6m19.toLong()
    val f6f7m38 = f[6] * f7m38.toLong()
    val f6f8m38 = f6m2 * f8m19.toLong()
    val f6f9m38 = f[6] * f9m38.toLong()
    val f7f7m38 = f[7] * f7m38.toLong()
    val f7f8m38 = f7m2 * f8m19.toLong()
    val f7f9m76 = f7m2 * f9m38.toLong()
    val f8f8m19 = f[8] * f8m19.toLong()
    val f8f9m38 = f[8] * f9m38.toLong()
    val f9f9m38 = f[9] * f9m38.toLong()

    /**
     * Same procedure as in multiply, but this time we have a higher symmetry
     * leading to less summands. e.g. f1f9_76 really stands for f1 * 2^26 * f9 *
     * 2^230 + f9 * 2^230 + f1 * 2^26 congruent 2 * 2 * 19 * f1 * f9 2^0 modulo p.
     */

    val h = longArrayOf(
        f0f0 + f1f9m76 + f2f8m38 + f3f7m76 + f4f6m38 + f5f5m38,
        f0f1m2 + f2f9m38 + f3f8m38 + f4f7m38 + f5f6m38,
        f0f2m2 + f1f1m2 + f3f9m76 + f4f8m38 + f5f7m76 + f6f6m19,
        f0f3m2 + f1f2m2 + f4f9m38 + f5f8m38 + f6f7m38,
        f0f4m2 + f1f3m4 + f2f2 + f5f9m76 + f6f8m38 + f7f7m38,
        f0f5m2 + f1f4m2 + f2f3m2 + f6f9m38 + f7f8m38,
        f0f6m2 + f1f5m4 + f2f4m2 + f3f3m2 + f7f9m76 + f8f8m19,
        f0f7m2 + f1f6m2 + f2f5m2 + f3f4m2 + f8f9m38,
        f0f8m2 + f1f7m4 + f2f6m2 + f3f5m4 + f4f4 + f9f9m38,
        f0f9m2 + f1f8m2 + f2f7m2 + f3f6m2 + f4f5m2
    ).map { it * multiplicator }.toLongArray()

    val carry = LongArray(10)

    carry[0] = h[0] + (1 shl 25).toLong() shr 26
    h[1] += carry[0]
    h[0] -= carry[0] shl 26
    carry[4] = h[4] + (1 shl 25).toLong() shr 26
    h[5] += carry[4]
    h[4] -= carry[4] shl 26
    carry[1] = h[1] + (1 shl 24).toLong() shr 25
    h[2] += carry[1]
    h[1] -= carry[1] shl 25
    carry[5] = h[5] + (1 shl 24).toLong() shr 25
    h[6] += carry[5]
    h[5] -= carry[5] shl 25
    carry[2] = h[2] + (1 shl 25).toLong() shr 26
    h[3] += carry[2]
    h[2] -= carry[2] shl 26
    carry[6] = h[6] + (1 shl 25).toLong() shr 26
    h[7] += carry[6]
    h[6] -= carry[6] shl 26
    carry[3] = h[3] + (1 shl 24).toLong() shr 25
    h[4] += carry[3]
    h[3] -= carry[3] shl 25
    carry[7] = h[7] + (1 shl 24).toLong() shr 25
    h[8] += carry[7]
    h[7] -= carry[7] shl 25
    carry[4] = h[4] + (1 shl 25).toLong() shr 26
    h[5] += carry[4]
    h[4] -= carry[4] shl 26
    carry[8] = h[8] + (1 shl 25).toLong() shr 26
    h[9] += carry[8]
    h[8] -= carry[8] shl 26
    carry[9] = h[9] + (1 shl 24).toLong() shr 25
    h[0] += carry[9] * 19
    h[9] -= carry[9] shl 25
    carry[0] = h[0] + (1 shl 25).toLong() shr 26
    h[1] += carry[0]
    h[0] -= carry[0] shl 26

    return FieldElement(h.map { it.toInt() }.toIntArray())
}