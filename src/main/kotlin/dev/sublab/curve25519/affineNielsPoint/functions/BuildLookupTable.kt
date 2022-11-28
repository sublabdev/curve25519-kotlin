package dev.sublab.curve25519.affineNielsPoint.functions

import dev.sublab.curve25519.affineNielsPoint.AffineNielsPoint
import dev.sublab.curve25519.affineNielsPoint.LookupTable
import dev.sublab.curve25519.edwardsPoint.EdwardsPoint
import dev.sublab.support.Array

/**
 * Construct a lookup table of $[P, [2]P, [3]P, [4]P, [5]P, [6]P, [7]P, [8]P]$.
 *
 * @param p the point to calculate multiples for.
 * @return the lookup table.
 */
internal fun AffineNielsPoint.Companion.buildLookupTable(p: EdwardsPoint) = LookupTable(Array(8) { prev, _ ->
    if (prev == null) p.toAffineNiels()
    else p.add(prev).toExtended().toAffineNiels()
})