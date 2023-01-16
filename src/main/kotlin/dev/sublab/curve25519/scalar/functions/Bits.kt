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
 * Construct a Scalar from the low 255 bits of a 256-bit integer.
 *
 *
 * This function is intended for applications like X25519 which require specific
 * bit-patterns when performing scalar multiplication.
 */
fun ByteArray.toScalarFromBits() = Scalar(copyOf().apply {
    // Ensure that s < 2^255 by masking the high bit
    this[31] = (this[31].toInt() and 0x7f).toByte()
})