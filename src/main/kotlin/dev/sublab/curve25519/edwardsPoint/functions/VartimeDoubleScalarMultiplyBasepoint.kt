package dev.sublab.curve25519.edwardsPoint.functions

import dev.sublab.curve25519.affineNielsPoint.AffineNielsPoint
import dev.sublab.curve25519.edwardsPoint.EdwardsPoint
import dev.sublab.curve25519.projectiveNielsPoint.ProjectiveNielsPoint
import dev.sublab.curve25519.projectiveNielsPoint.functions.buildNafLookupTable
import dev.sublab.curve25519.scalar.Scalar
import dev.sublab.curve25519.scalar.functions.nonAdjacentForm

/**
 * Compute $r = [a]A + [b]B$ in variable time, where $B$ is the Ed25519
 * basepoint.
 *
 * @param a a Scalar.
 * @param ep an EdwardsPoint.
 * @param b a Scalar.
 * @return $[a]A + [b]B$
 */
fun vartimeDoubleScalarMultiplyBasepoint(
    a: Scalar,
    ep: EdwardsPoint,
    b: Scalar
): EdwardsPoint {
    val aNaf = a.nonAdjacentForm()
    val bNaf = b.nonAdjacentForm()
    val tableA = ProjectiveNielsPoint.buildNafLookupTable(ep)
    val tableB = AffineNielsPoint.AFFINE_ODD_MULTIPLES_OF_BASEPOINT

    var i = 255
    while (i >= 0) {
        if (aNaf[i].toInt() != 0 || bNaf[i].toInt() != 0) break
        --i
    }

    var r = EdwardsPoint.IDENTITY.toProjective()
    while (i >= 0) {
        var t = r.double()

        if (aNaf[i] > 0) {
            t = t.toExtended().add(tableA.select(aNaf[i]))
        } else if (aNaf[i] < 0) {
            t = t.toExtended().subtract(tableA.select(-aNaf[i]))
        }

        if (bNaf[i] > 0) {
            t = t.toExtended().add(tableB.select(bNaf[i]))
        } else if (bNaf[i] < 0) {
            t = t.toExtended().subtract(tableB.select(-bNaf[i]))
        }

        r = t.toProjective()
        --i
    }

    return r.toExtended()
}