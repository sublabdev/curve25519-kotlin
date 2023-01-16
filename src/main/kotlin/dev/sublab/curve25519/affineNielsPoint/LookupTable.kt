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

package dev.sublab.curve25519.affineNielsPoint

import dev.sublab.curve25519.CommonLookupTable
import dev.sublab.curve25519.affineNielsPoint.AffineNielsPoint.Companion.IDENTITY
import dev.sublab.support.ConstantTime

internal class LookupTable(private val table: Array<AffineNielsPoint>): CommonLookupTable<AffineNielsPoint> {
    private val selections = hashMapOf<Int, AffineNielsPoint>()

    /**
     * Given $-8 \leq x \leq 8$, return $[x]P$ in constant time.
     *
     * @param x the index.
     * @return the pre-computed point.
     */
    override fun select(x: Int): AffineNielsPoint {
        require(!(x < -8 || x > 8)) { "x is not in range -8 <= x <= 8" }
        selections[x]?.let { return it }

        val xNegative = if (ConstantTime.isNegative(x)) 1 else 0

        // |x|
        val xabs = x - (-xNegative and x shl 1)

        // |x| P
        var t = IDENTITY
        for (i in 1..8) {
            t = t.ctSelect(table[i - 1], if (ConstantTime.equal(xabs, i)) 1 else 0)
        }

        // -|x| P
        val tminus = t.negate()

        // [x]P
        val selection = t.ctSelect(tminus, xNegative)

        selections[x] = selection
        return selection
    }
}