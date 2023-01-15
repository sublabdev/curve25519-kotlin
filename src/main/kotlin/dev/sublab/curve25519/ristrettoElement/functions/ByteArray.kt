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

package dev.sublab.curve25519.ristrettoElement.functions

import dev.sublab.curve25519.fieldElement.functions.toFieldElement
import dev.sublab.curve25519.ristrettoElement.RistrettoElement

/**
 * Construct a ristretto255 element from a uniformly-distributed 64-byte string.
 *
 *
 * This is the ristretto255 FROM_UNIFORM_BYTES function.
 *
 * @return the resulting element.
 */
fun ByteArray.toRistrettoElement(): RistrettoElement {
    // 1. Interpret the least significant 255 bits of b[ 0..32] as an
    // integer r1 in little-endian representation. Reduce r0 modulo p.
    val b1 = copyOf(32)
    val r1 = b1.toFieldElement()

    // 2. Interpret the least significant 255 bits of b[32..64] as an
    // integer r2 in little-endian representation. Reduce r2 modulo p.
    val b2 = copyOfRange(32, 64)
    val r2 = b2.toFieldElement()

    // 3. Compute group element P1 as MAP(r0)
    val p1 = r1.map()

    // 4. Compute group element P2 as MAP(r1).
    val p2 = r2.map()

    // 5. Return the group element P1 + P2.
    return p1.add(p2)
}