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

package dev.sublab.curve25519.unpackedScalar.functions

import dev.sublab.curve25519.unpackedScalar.*
import dev.sublab.curve25519.unpackedScalar.UnpackedScalar.Companion.L
import dev.sublab.curve25519.unpackedScalar.UnpackedScalar.Companion.LFACTOR

/**
 * Compute $\text{limbs}/R \bmod \ell$, where R is the Montgomery modulus 2^261.
 *
 * @param limbs the value to reduce.
 * @return $\text{limbs}/R \bmod \ell$
 */
internal fun montgomeryReduce(limbs: LongArray): UnpackedScalar {
    // Note: l5,l6,l7 are zero, so their multiplies can be skipped
    val l = L.s

    var carry: Long
    val r = IntArray(9)
    val n = IntArray(9)

    // The first half computes the Montgomery adjustment factor n, and begins adding
    // n*l to make limbs divisible by R
    var sum: Long = (limbs[0])
    n[0] = (((sum and 0xffffffffL) * LFACTOR) and MASK_29_BITS.toLong()).toInt()
    carry = (sum + m(n[0], l[0])) ushr 29
    sum = (carry + limbs[1] + m(n[0], l[1]))
    n[1] = (((sum and 0xffffffffL) * LFACTOR) and MASK_29_BITS.toLong()).toInt()
    carry = (sum + m(n[1], l[0])) ushr 29
    sum = (carry + limbs[2] + m(n[0], l[2]) + m(n[1], l[1]))
    n[2] = (((sum and 0xffffffffL) * LFACTOR) and MASK_29_BITS.toLong()).toInt()
    carry = (sum + m(n[2], l[0])) ushr 29
    sum = (carry + limbs[3] + m(n[0], l[3]) + m(n[1], l[2]) + m(n[2], l[1]))
    n[3] = (((sum and 0xffffffffL) * LFACTOR) and MASK_29_BITS.toLong()).toInt()
    carry = (sum + m(n[3], l[0])) ushr 29
    sum = (carry + limbs[4] + m(n[0], l[4]) + m(n[1], l[3]) + m(n[2], l[2]) + m(n[3], l[1]))
    n[4] = (((sum and 0xffffffffL) * LFACTOR) and MASK_29_BITS.toLong()).toInt()
    carry = (sum + m(n[4], l[0])) ushr 29
    sum = (carry + limbs[5] + m(n[1], l[4]) + m(n[2], l[3]) + m(n[3], l[2]) + m(n[4], l[1]))
    n[5] = (((sum and 0xffffffffL) * LFACTOR) and MASK_29_BITS.toLong()).toInt()
    carry = (sum + m(n[5], l[0])) ushr 29
    sum = (carry + limbs[6] + m(n[2], l[4]) + m(n[3], l[3]) + m(n[4], l[2]) + m(n[5], l[1]))
    n[6] = (((sum and 0xffffffffL) * LFACTOR) and MASK_29_BITS.toLong()).toInt()
    carry = (sum + m(n[6], l[0])) ushr 29
    sum = (carry + limbs[7] + m(n[3], l[4]) + m(n[4], l[3]) + m(n[5], l[2]) + m(n[6], l[1]))
    n[7] = (((sum and 0xffffffffL) * LFACTOR) and MASK_29_BITS.toLong()).toInt()
    carry = (sum + m(n[7], l[0])) ushr 29
    sum = (carry + limbs[8] + m(n[0], l[8]) + m(n[4], l[4]) + m(n[5], l[3]) + m(n[6], l[2]) + m(n[7], l[1]))
    n[8] = (((sum and 0xffffffffL) * LFACTOR) and MASK_29_BITS.toLong()).toInt()
    carry = (sum + m(n[8], l[0])) ushr 29

    // limbs are divisible by R now, so we can divide by R by simply storing the upper half as the result
    sum = (carry + limbs[9] + m(n[1], l[8]) + m(n[5], l[4]) + m(n[6], l[3]) + m(n[7], l[2]) + m(n[8], l[1]))
    r[0] = (sum and MASK_29_BITS.toLong()).toInt()
    carry = sum ushr 29
    sum = (carry + limbs[10] + m(n[2], l[8]) + m(n[6], l[4]) + m(n[7], l[3]) + m(n[8], l[2]))
    r[1] = (sum and MASK_29_BITS.toLong()).toInt()
    carry = sum ushr 29
    sum = (carry + limbs[11] + m(n[3], l[8]) + m(n[7], l[4]) + m(n[8], l[3]))
    r[2] = (sum and MASK_29_BITS.toLong()).toInt()
    carry = sum ushr 29
    sum = (carry + limbs[12] + m(n[4], l[8]) + m(n[8], l[4]))
    r[3] = (sum and MASK_29_BITS.toLong()).toInt()
    carry = sum ushr 29
    sum = (carry + limbs[13] + m(n[5], l[8]))
    r[4] = (sum and MASK_29_BITS.toLong()).toInt()
    carry = sum ushr 29
    sum = (carry + limbs[14] + m(n[6], l[8]))
    r[5] = (sum and MASK_29_BITS.toLong()).toInt()
    carry = sum ushr 29
    sum = (carry + limbs[15] + m(n[7], l[8]))
    r[6] = (sum and MASK_29_BITS.toLong()).toInt()
    carry = sum ushr 29
    sum = (carry + limbs[16] + m(n[8], l[8]))
    r[7] = (sum and MASK_29_BITS.toLong()).toInt()
    carry = sum ushr 29
    r[8] = (carry and 0xffffffffL).toInt()

    // Result may be >= l, so attempt to subtract l
    return UnpackedScalar(r).subtract(L)
}