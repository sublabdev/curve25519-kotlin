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

package dev.sublab.curve25519.projectiveNielsPoint

import dev.sublab.curve25519.CommonLookupTable

internal class NafLookupTable(private val table: Array<ProjectiveNielsPoint>): CommonLookupTable<ProjectiveNielsPoint> {
    /**
     * Given public, odd $x$ with $0 \lt x \lt 2^4$, return $[x]A$.
     *
     * @param x the index.
     * @return the pre-computed point.
     */
    override fun select(x: Int): ProjectiveNielsPoint {
        require(!(x % 2 == 0 || x >= 16)) { "invalid x" }
        return table[x / 2]
    }
}