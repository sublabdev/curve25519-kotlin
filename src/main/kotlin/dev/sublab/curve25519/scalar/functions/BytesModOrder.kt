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

import dev.sublab.curve25519.fieldElement.functions.load3
import dev.sublab.curve25519.fieldElement.functions.load4
import dev.sublab.curve25519.scalar.Scalar

/**
 * Construct a Scalar by reducing a 256-bit little-endian integer modulo the
 * group order $\ell$.
 */
fun ByteArray.toScalarBytesModOrder(): Scalar {
    require(size == 32) { "Input must by 32 bytes" }
    return reduceToScalar()
}

/**
 * Construct a Scalar by reducing a 512-bit little-endian integer modulo the
 * group order $\ell$.
 */
fun ByteArray.toScalarBytesModOrderWide(): Scalar {
    require(size == 64) { "Input must by 64 bytes" }

    // s[0],..., s[22] have 21 bits, s[23] has 29 bits
    val sl = 0x1FFFFF.toLong() // need long
    val s = longArrayOf(
        sl and load3(this, 0),
        sl and (load4(this, 2) shr 5),
        sl and (load3(this, 5) shr 2),
        sl and (load4(this, 7) shr 7),
        sl and (load4(this, 10) shr 4),
        sl and (load3(this, 13) shr 1),
        sl and (load4(this, 15) shr 6),
        sl and (load3(this, 18) shr 3),
        sl and load3(this, 21),
        sl and (load4(this, 23) shr 5),
        sl and (load3(this, 26) shr 2),
        sl and (load4(this, 28) shr 7),
        sl and (load4(this, 31) shr 4),
        sl and (load3(this, 34) shr 1),
        sl and (load4(this, 36) shr 6),
        sl and (load3(this, 39) shr 3),
        sl and load3(this, 42),
        sl and (load4(this, 44) shr 5),
        sl and (load3(this, 47) shr 2),
        sl and (load4(this, 49) shr 7),
        sl and (load4(this, 52) shr 4),
        sl and (load3(this, 55) shr 1),
        sl and (load4(this, 57) shr 6),
        load4(this, 60) shr 3
    )

    val carry = LongArray(17)
    return op1(s, carry)
}