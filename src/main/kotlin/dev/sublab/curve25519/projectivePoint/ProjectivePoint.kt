package dev.sublab.curve25519.projectivePoint

import dev.sublab.curve25519.completedPoint.CompletedPoint
import dev.sublab.curve25519.edwardsPoint.EdwardsPoint
import dev.sublab.curve25519.fieldElement.FieldElement
import dev.sublab.curve25519.fieldElement.functions.multiply
import dev.sublab.curve25519.fieldElement.functions.square
import dev.sublab.curve25519.fieldElement.functions.squareAndDouble

/**
 * A point $(X:Y:Z)$ on the $\mathbb P^2$ model of the curve.
 */
internal class ProjectivePoint(
    val x: FieldElement,
    val y: FieldElement,
    val z: FieldElement
) {
    /**
     * Convert this point from the $\mathbb P^2$ model to the $\mathbb P^3$ model.
     *
     *
     * This costs $3 \mathrm M + 1 \mathrm S$.
     */
    fun toExtended() = EdwardsPoint(x.multiply(z), y.multiply(z), z.square(), x.multiply(y))

    /**
     * Point doubling: add this point to itself.
     *
     * @return $[2]P$ as a CompletedPoint.
     */
    fun double(): CompletedPoint {
        val xx = x.square()
        val yy = y.square()
        val zz2 = z.squareAndDouble()
        val xPlusY = x.add(y)
        val xPlusYSq = xPlusY.square()
        val yyPlusXx = yy.add(xx)
        val yyMinusXx = yy.subtract(xx)

        return CompletedPoint(xPlusYSq.subtract(yyPlusXx), yyPlusXx, yyMinusXx, zz2.subtract(yyMinusXx))
    }
}