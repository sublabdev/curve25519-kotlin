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

package dev.sublab.curve25519.affineNielsPoint.functions

import dev.sublab.curve25519.affineNielsPoint.AffineNielsPoint
import dev.sublab.curve25519.affineNielsPoint.NafLookupTable
import dev.sublab.curve25519.edwardsPoint.EdwardsPoint
import dev.sublab.support.Array

/**
 * Construct a lookup table of $[P, [3]P, [5]P, [7]P, [9]P, [11]P, [13]P,
 * [15]P]$.
 *
 * @param p the point to calculate multiples for.
 * @return the lookup table.
 */
internal fun AffineNielsPoint.Companion.buildNafLookupTable(p: EdwardsPoint): NafLookupTable {
    val p2 = p.double()
    return NafLookupTable(Array(8) { prev, _ ->
        if (prev == null) p.toAffineNiels()
        else p2.add(prev).toExtended().toAffineNiels()
    })
}