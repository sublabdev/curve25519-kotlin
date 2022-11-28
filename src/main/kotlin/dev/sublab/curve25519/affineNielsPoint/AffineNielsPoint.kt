package dev.sublab.curve25519.affineNielsPoint

import dev.sublab.curve25519.affineNielsPoint.functions.buildNafLookupTable
import dev.sublab.curve25519.edwardsPoint.EdwardsPoint
import dev.sublab.curve25519.fieldElement.FieldElement

/**
 * A pre-computed point on the affine model of the curve, represented as $(y+x,
 * y-x, 2dxy)$ in "Niels coordinates".
 */
internal class AffineNielsPoint(
    val yPlusX: FieldElement,
    val yMinusX: FieldElement,
    val xy2D: FieldElement
) {
    /**
     * Constant-time selection between two AffineNielsPoints.
     *
     * @param that the other point.
     * @param b    must be 0 or 1, otherwise results are undefined.
     * @return a copy of this if $b == 0$, or a copy of that if $b == 1$.
     */
    fun ctSelect(that: AffineNielsPoint, b: Int) = AffineNielsPoint(
        yPlusX.ctSelect(that.yPlusX, b),
        yMinusX.ctSelect(that.yMinusX, b),
        xy2D.ctSelect(that.xy2D, b)
    )

    /**
     * Point negation.
     *
     * @return $-P$
     */
    fun negate() = AffineNielsPoint(yMinusX, yPlusX, xy2D.negate())

    companion object {
        val IDENTITY = AffineNielsPoint(
            FieldElement.ONE,
            FieldElement.ONE,
            FieldElement.ZERO
        )

        /**
         * Odd multiples of the Ed25519 basepoint.
         */
        val AFFINE_ODD_MULTIPLES_OF_BASEPOINT = AffineNielsPoint.buildNafLookupTable(
            EdwardsPoint.ED25519_BASEPOINT
        )
    }
}