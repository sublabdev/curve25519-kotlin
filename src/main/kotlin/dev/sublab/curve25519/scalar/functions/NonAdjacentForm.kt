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
 * Compute a width-$w$ "Non-Adjacent Form" of this scalar.
 *
 * @return The byte array $naf$ in the above described form.
 */
fun Scalar.nonAdjacentForm(): ByteArray {
    val naf = ByteArray(256)

    // Put each bit of this into a separate byte, 0 or 1
    for (i in 0..255) {
        naf[i] = (1 and (s[i shr 3].toInt() shr (i and 7))).toByte()
    }

    // Note: naf[i] will always be odd.
    for (i in 0..255) {
        if (naf[i].toInt() == 0) continue

        var b = 1
        while (b <= 6 && i + b < 256) {
            // Accumulate bits if possible
            if (naf[i + b].toInt() != 0) {
                if (naf[i] + (naf[i + b].toInt() shl b) <= 15) {
                    naf[i] = (naf[i].toInt() + (naf[i+b].toInt() shl b)).toByte()
                    naf[i + b] = 0
                } else if (naf[i] - (naf[i + b].toInt() shl b) >= -15) {
                    naf[i] = (naf[i].toInt() - (naf[i+b].toInt() shl b)).toByte()
                    for (k in i + b..255) {
                        if (naf[k].toInt() == 0) {
                            naf[k] = 1
                            break
                        }
                        naf[k] = 0
                    }
                } else break
            }
            ++b
        }
    }

    return naf
}