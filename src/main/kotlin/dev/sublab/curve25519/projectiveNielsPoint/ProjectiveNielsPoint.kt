package dev.sublab.curve25519.projectiveNielsPoint

import dev.sublab.curve25519.fieldElement.FieldElement

/**
 * A pre-computed point on the $\mathbb P^3$ model of the curve, represented as
 * $(Y+X, Y-X, Z, 2dXY)$ in "Niels coordinates".
 */
internal class ProjectiveNielsPoint(
    val yPlusX: FieldElement,
    val yMinusX: FieldElement,
    val z: FieldElement,
    val t2D: FieldElement
) {
    /**
     * Constant-time selection between two ProjectiveNielsPoints.
     *
     * @param that the other point.
     * @param b    must be 0 or 1, otherwise results are undefined.
     * @return a copy of this if $b == 0$, or a copy of that if $b == 1$.
     */
    fun ctSelect(that: ProjectiveNielsPoint, b: Int) = ProjectiveNielsPoint(
        yPlusX.ctSelect(that.yPlusX, b),
        yMinusX.ctSelect(that.yMinusX, b),
        z.ctSelect(that.z, b),
        t2D.ctSelect(that.t2D, b)
    )

    /**
     * Point negation.
     *
     * @return $-P$
     */
    fun negate() = ProjectiveNielsPoint(yMinusX, yPlusX, z, t2D.negate())

    companion object {
        val IDENTITY = ProjectiveNielsPoint(
            FieldElement.ONE,
            FieldElement.ONE,
            FieldElement.ONE,
            FieldElement.ZERO
        )
    }
}