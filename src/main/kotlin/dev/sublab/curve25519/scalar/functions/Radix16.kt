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
 * Writes this Scalar in radix 16, with coefficients in range $[-8, 8)$.
 *
 * @return 64 bytes, each between -8 and 7
 */
fun Scalar.toRadix16(): ByteArray {
    val e = ByteArray(64)
    // Radix 16 notation
    for ((i, byte) in s.withIndex()) {
        e[i*2] = (byte.toInt() and 15).toByte()
        e[i*2 + 1] = ((byte.toInt() shr 4) and 15).toByte()
    }

    /* each e[i] is between 0 and 15 */
    /* e[63] is between 0 and 7 */
    var carry = 0
    for (i in e.indices) {
        e[i] = (e[i].toInt() + carry).toByte()
        carry = e[i].toInt() + 8
        carry = carry shr 4
        e[i] = (e[i].toInt() - (carry shl 4)).toByte()
    }

    e[63] = (e[63].toInt() + carry).toByte()
    /* each e[i] is between -8 and 7 */
    return e
}