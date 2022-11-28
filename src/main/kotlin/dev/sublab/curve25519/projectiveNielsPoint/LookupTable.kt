package dev.sublab.curve25519.projectiveNielsPoint

import dev.sublab.curve25519.CommonLookupTable
import dev.sublab.curve25519.projectiveNielsPoint.ProjectiveNielsPoint.Companion.IDENTITY
import dev.sublab.support.ConstantTime

internal class LookupTable(private val table: Array<ProjectiveNielsPoint>): CommonLookupTable<ProjectiveNielsPoint> {
    private val selections = hashMapOf<Int, ProjectiveNielsPoint>()

    /**
     * Given $-8 \leq x \leq 8$, return $[x]P$ in constant time.
     *
     * @param x the index.
     * @return the pre-computed point.
     */
    override fun select(x: Int): ProjectiveNielsPoint {
        require(!(x < -8 || x > 8)) { "x is not in range -8 <= x <= 8" }
        selections[x]?.let { return it }

        val xNegative: Int = if (ConstantTime.isNegative(x)) 1 else 0
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