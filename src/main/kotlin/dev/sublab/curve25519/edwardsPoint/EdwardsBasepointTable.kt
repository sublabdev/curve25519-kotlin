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

package dev.sublab.curve25519.edwardsPoint

import dev.sublab.curve25519.affineNielsPoint.AffineNielsPoint
import dev.sublab.curve25519.affineNielsPoint.LookupTable
import dev.sublab.curve25519.affineNielsPoint.functions.buildLookupTable
import dev.sublab.curve25519.edwardsPoint.EdwardsPoint.Companion.IDENTITY
import dev.sublab.curve25519.scalar.Scalar
import dev.sublab.curve25519.scalar.functions.toRadix16

@Suppress("NAME_SHADOWING")
private fun buildLookupTables(basepoint: EdwardsPoint): Array<LookupTable> {
    var basepoint = basepoint
    return Array(32) {
        val current = AffineNielsPoint.buildLookupTable(basepoint)
        // Only every second summand is precomputed (16^2 = 256)
        basepoint = basepoint.multiplyByPow2(8)
        current
    }
}

/**
 * A pre-computed table of multiples of a basepoint, for accelerating fixed-base
 * scalar multiplication.
 */
class EdwardsBasepointTable(basepoint: EdwardsPoint) {
    /**
     * Create a table of pre-computed multiples of basepoint.
     */
    private val tables = buildLookupTables(basepoint)

    /**
     * Constant-time fixed-base scalar multiplication.
     *
     * @param s the Scalar to multiply by.
     * @return $[s]B$
     */
    fun multiply(s: Scalar): EdwardsPoint {
        val e = s.toRadix16()
        var h = IDENTITY

        var i = 1
        while (i < 64) {
            h = h.add(tables[i / 2].select(e[i])).toExtended()
            i += 2
        }

        h = h.multiplyByPow2(4)
        i = 0
        while (i < 64) {
            h = h.add(tables[i / 2].select(e[i])).toExtended()
            i += 2
        }

        return h
    }
}