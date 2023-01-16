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

package dev.sublab.curve25519.scalar.functions

import dev.sublab.curve25519.scalar.Scalar

/**
 * Common operation between [Scalar.multiplyAndAdd] and [ByteArray.toScalarBytesModOrderWide]
 */
internal fun op1(s: LongArray, carry: LongArray): Scalar {
    /**
     * Lots of magic numbers :)
     * To understand what's going on below, note that
     *
     * (1) q = 2^252 + q0 where q0 = 27742317777372353535851937790883648493.
     * (2) s[11] is the coefficient of 2^(11*21), s[23] is the coefficient of 2^(23*21) and 2^252 = 2^((23-11) * 21)).
     * (3) 2^252 congruent -q0 modulo q.
     * (4) -q0 = 666643 * 2^0 + 470296 * 2^21 + 654183 * 2^(2*21) - 997805 * 2^(3*21) + 136657 * 2^(4*21) - 683901 * 2^(5*21)
     *
     * Thus
     * s[23] * 2^(23*11) = s[23] * 2^(12*21) * 2^(11*21) = s[23] * 2^252 * 2^(11*21) congruent
     * s[23] * (666643 * 2^0 + 470296 * 2^21 + 654183 * 2^(2*21) - 997805 * 2^(3*21) + 136657 * 2^(4*21) - 683901 * 2^(5*21)) * 2^(11*21) modulo q =
     * s[23] * (666643 * 2^(11*21) + 470296 * 2^(12*21) + 654183 * 2^(13*21) - 997805 * 2^(14*21) + 136657 * 2^(15*21) - 683901 * 2^(16*21)).
     *
     * The same procedure is then applied for s[22],...,s[18].
     */
    s[11] += s[23] * 666643
    s[12] += s[23] * 470296
    s[13] += s[23] * 654183
    s[14] -= s[23] * 997805
    s[15] += s[23] * 136657
    s[16] -= s[23] * 683901
    s[10] += s[22] * 666643
    s[11] += s[22] * 470296
    s[12] += s[22] * 654183
    s[13] -= s[22] * 997805
    s[14] += s[22] * 136657
    s[15] -= s[22] * 683901
    s[9] += s[21] * 666643
    s[10] += s[21] * 470296
    s[11] += s[21] * 654183
    s[12] -= s[21] * 997805
    s[13] += s[21] * 136657
    s[14] -= s[21] * 683901
    s[8] += s[20] * 666643
    s[9] += s[20] * 470296
    s[10] += s[20] * 654183
    s[11] -= s[20] * 997805
    s[12] += s[20] * 136657
    s[13] -= s[20] * 683901
    s[7] += s[19] * 666643
    s[8] += s[19] * 470296
    s[9] += s[19] * 654183
    s[10] -= s[19] * 997805
    s[11] += s[19] * 136657
    s[12] -= s[19] * 683901
    s[6] += s[18] * 666643
    s[7] += s[18] * 470296
    s[8] += s[18] * 654183
    s[9] -= s[18] * 997805
    s[10] += s[18] * 136657
    s[11] -= s[18] * 683901

    /**
     * Time to reduce the coefficient in order not to get an overflow.
     */
    carry[6] = s[6] + (1 shl 20) shr 21
    s[7] += carry[6]
    s[6] -= carry[6] shl 21
    carry[8] = s[8] + (1 shl 20) shr 21
    s[9] += carry[8]
    s[8] -= carry[8] shl 21
    carry[10] = s[10] + (1 shl 20) shr 21
    s[11] += carry[10]
    s[10] -= carry[10] shl 21
    carry[12] = s[12] + (1 shl 20) shr 21
    s[13] += carry[12]
    s[12] -= carry[12] shl 21
    carry[14] = s[14] + (1 shl 20) shr 21
    s[15] += carry[14]
    s[14] -= carry[14] shl 21
    carry[16] = s[16] + (1 shl 20) shr 21
    s[17] += carry[16]
    s[16] -= carry[16] shl 21
    carry[7] = s[7] + (1 shl 20) shr 21
    s[8] += carry[7]
    s[7] -= carry[7] shl 21
    carry[9] = s[9] + (1 shl 20) shr 21
    s[10] += carry[9]
    s[9] -= carry[9] shl 21
    carry[11] = s[11] + (1 shl 20) shr 21
    s[12] += carry[11]
    s[11] -= carry[11] shl 21
    carry[13] = s[13] + (1 shl 20) shr 21
    s[14] += carry[13]
    s[13] -= carry[13] shl 21
    carry[15] = s[15] + (1 shl 20) shr 21
    s[16] += carry[15]
    s[15] -= carry[15] shl 21

    /**
     * Continue with above procedure.
     */
    s[5] += s[17] * 666643
    s[6] += s[17] * 470296
    s[7] += s[17] * 654183
    s[8] -= s[17] * 997805
    s[9] += s[17] * 136657
    s[10] -= s[17] * 683901
    s[4] += s[16] * 666643
    s[5] += s[16] * 470296
    s[6] += s[16] * 654183
    s[7] -= s[16] * 997805
    s[8] += s[16] * 136657
    s[9] -= s[16] * 683901
    s[3] += s[15] * 666643
    s[4] += s[15] * 470296
    s[5] += s[15] * 654183
    s[6] -= s[15] * 997805
    s[7] += s[15] * 136657
    s[8] -= s[15] * 683901
    s[2] += s[14] * 666643
    s[3] += s[14] * 470296
    s[4] += s[14] * 654183
    s[5] -= s[14] * 997805
    s[6] += s[14] * 136657
    s[7] -= s[14] * 683901
    s[1] += s[13] * 666643
    s[2] += s[13] * 470296
    s[3] += s[13] * 654183
    s[4] -= s[13] * 997805
    s[5] += s[13] * 136657
    s[6] -= s[13] * 683901
    s[0] += s[12] * 666643
    s[1] += s[12] * 470296
    s[2] += s[12] * 654183
    s[3] -= s[12] * 997805
    s[4] += s[12] * 136657
    s[5] -= s[12] * 683901

    /**
     * Reduce coefficients again.
     */
    carry[0] = s[0] + (1 shl 20) shr 21
    s[1] += carry[0]
    s[0] -= carry[0] shl 21
    carry[2] = s[2] + (1 shl 20) shr 21
    s[3] += carry[2]
    s[2] -= carry[2] shl 21
    carry[4] = s[4] + (1 shl 20) shr 21
    s[5] += carry[4]
    s[4] -= carry[4] shl 21
    carry[6] = s[6] + (1 shl 20) shr 21
    s[7] += carry[6]
    s[6] -= carry[6] shl 21
    carry[8] = s[8] + (1 shl 20) shr 21
    s[9] += carry[8]
    s[8] -= carry[8] shl 21
    carry[10] = s[10] + (1 shl 20) shr 21
    s[11] += carry[10]
    s[10] -= carry[10] shl 21
    carry[1] = s[1] + (1 shl 20) shr 21
    s[2] += carry[1]
    s[1] -= carry[1] shl 21
    carry[3] = s[3] + (1 shl 20) shr 21
    s[4] += carry[3]
    s[3] -= carry[3] shl 21
    carry[5] = s[5] + (1 shl 20) shr 21
    s[6] += carry[5]
    s[5] -= carry[5] shl 21
    carry[7] = s[7] + (1 shl 20) shr 21
    s[8] += carry[7]
    s[7] -= carry[7] shl 21
    carry[9] = s[9] + (1 shl 20) shr 21
    s[10] += carry[9]
    s[9] -= carry[9] shl 21
    carry[11] = s[11] + (1 shl 20) shr 21
    s[12] = carry[11]
    s[11] -= carry[11] shl 21

    s[0] += s[12] * 666643
    s[1] += s[12] * 470296
    s[2] += s[12] * 654183
    s[3] -= s[12] * 997805
    s[4] += s[12] * 136657
    s[5] -= s[12] * 683901

    carry[0] = s[0] shr 21
    s[1] += carry[0]
    s[0] -= carry[0] shl 21
    carry[1] = s[1] shr 21
    s[2] += carry[1]
    s[1] -= carry[1] shl 21
    carry[2] = s[2] shr 21
    s[3] += carry[2]
    s[2] -= carry[2] shl 21
    carry[3] = s[3] shr 21
    s[4] += carry[3]
    s[3] -= carry[3] shl 21
    carry[4] = s[4] shr 21
    s[5] += carry[4]
    s[4] -= carry[4] shl 21
    carry[5] = s[5] shr 21
    s[6] += carry[5]
    s[5] -= carry[5] shl 21
    carry[6] = s[6] shr 21
    s[7] += carry[6]
    s[6] -= carry[6] shl 21
    carry[7] = s[7] shr 21
    s[8] += carry[7]
    s[7] -= carry[7] shl 21
    carry[8] = s[8] shr 21
    s[9] += carry[8]
    s[8] -= carry[8] shl 21
    carry[9] = s[9] shr 21
    s[10] += carry[9]
    s[9] -= carry[9] shl 21
    carry[10] = s[10] shr 21
    s[11] += carry[10]
    s[10] -= carry[10] shl 21
    carry[11] = s[11] shr 21
    s[12] = carry[11]
    s[11] -= carry[11] shl 21

    s[0] += s[12] * 666643
    s[1] += s[12] * 470296
    s[2] += s[12] * 654183
    s[3] -= s[12] * 997805
    s[4] += s[12] * 136657
    s[5] -= s[12] * 683901

    carry[0] = s[0] shr 21
    s[1] += carry[0]
    s[0] -= carry[0] shl 21
    carry[1] = s[1] shr 21
    s[2] += carry[1]
    s[1] -= carry[1] shl 21
    carry[2] = s[2] shr 21
    s[3] += carry[2]
    s[2] -= carry[2] shl 21
    carry[3] = s[3] shr 21
    s[4] += carry[3]
    s[3] -= carry[3] shl 21
    carry[4] = s[4] shr 21
    s[5] += carry[4]
    s[4] -= carry[4] shl 21
    carry[5] = s[5] shr 21
    s[6] += carry[5]
    s[5] -= carry[5] shl 21
    carry[6] = s[6] shr 21
    s[7] += carry[6]
    s[6] -= carry[6] shl 21
    carry[7] = s[7] shr 21
    s[8] += carry[7]
    s[7] -= carry[7] shl 21
    carry[8] = s[8] shr 21
    s[9] += carry[8]
    s[8] -= carry[8] shl 21
    carry[9] = s[9] shr 21
    s[10] += carry[9]
    s[9] -= carry[9] shl 21
    carry[10] = s[10] shr 21
    s[11] += carry[10]
    s[10] -= carry[10] shl 21

    // s[0], ..., s[11] got 21 bits each.
    val result = longArrayOf(
        s[0], s[0] shr 8, s[0] shr 16 or (s[1] shl 5),
        s[1] shr 3, s[1] shr 11, s[1] shr 19 or (s[2] shl 2),
        s[2] shr 6, s[2] shr 14 or (s[3] shl 7),
        s[3] shr 1, s[3] shr 9, s[3] shr 17 or (s[4] shl 4),
        s[4] shr 4, s[4] shr 12, s[4] shr 20 or (s[5] shl 1),
        s[5] shr 7, s[5] shr 15 or (s[6] shl 6),
        s[6] shr 2, s[6] shr 10, s[6] shr 18 or (s[7] shl 3),
        s[7] shr 5, s[7] shr 13,
        s[8], s[8] shr 8, s[8] shr 16 or (s[9] shl 5),
        s[9] shr 3, s[9] shr 11, s[9] shr 19 or (s[10] shl 2),
        s[10] shr 6, s[10] shr 14 or (s[11] shl 7),
        s[11] shr 1, s[11] shr 9, s[11] shr 17
    )

    return Scalar(result.map { it.toByte() }.toByteArray())
}