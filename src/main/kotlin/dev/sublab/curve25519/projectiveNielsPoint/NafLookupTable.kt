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