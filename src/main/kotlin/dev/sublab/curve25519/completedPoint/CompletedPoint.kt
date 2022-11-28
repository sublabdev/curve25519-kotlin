package dev.sublab.curve25519.completedPoint

import dev.sublab.curve25519.edwardsPoint.EdwardsPoint
import dev.sublab.curve25519.fieldElement.FieldElement
import dev.sublab.curve25519.fieldElement.functions.multiply
import dev.sublab.curve25519.projectivePoint.ProjectivePoint

/**
 * A point $((X:Z), (Y:T))$ on the $\mathbb P^1 \times \mathbb P^1$ model of the
 * curve.
 */
internal class CompletedPoint(
    val x: FieldElement,
    val y: FieldElement,
    val z: FieldElement,
    val t: FieldElement
) {
    /**
     * Convert this point from the $\mathbb P^1 \times \mathbb P^1$ model to the
     * $\mathbb P^2$ model.
     *
     *
     * This costs $3 \mathrm M$.
     */
    fun toProjective() = ProjectivePoint(x.multiply(t), y.multiply(z), z.multiply(t))

    /**
     * Convert this point from the $\mathbb P^1 \times \mathbb P^1$ model to the
     * $\mathbb P^3$ model.
     *
     *
     * This costs $4 \mathrm M$.
     */
    fun toExtended() = EdwardsPoint(x.multiply(t), y.multiply(z), z.multiply(t), x.multiply(y))
}