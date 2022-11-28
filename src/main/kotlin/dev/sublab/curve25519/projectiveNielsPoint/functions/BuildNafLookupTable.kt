package dev.sublab.curve25519.projectiveNielsPoint.functions

import dev.sublab.curve25519.edwardsPoint.EdwardsPoint
import dev.sublab.curve25519.projectiveNielsPoint.NafLookupTable
import dev.sublab.curve25519.projectiveNielsPoint.ProjectiveNielsPoint
import dev.sublab.support.Array

/**
 * Construct a lookup table of $[P, [3]P, [5]P, [7]P, [9]P, [11]P, [13]P,
 * [15]P]$.
 *
 * @param p the point to calculate multiples for.
 * @return the lookup table.
 */
internal fun ProjectiveNielsPoint.Companion.buildNafLookupTable(p: EdwardsPoint): NafLookupTable {
    val p2 = p.double()
    return NafLookupTable(Array(8) { prev, _ ->
        if (prev == null) p.toProjectiveNiels()
        else p2.add(prev).toExtended().toProjectiveNiels()
    })
}