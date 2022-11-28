package dev.sublab.curve25519.ristrettoElement

import dev.sublab.curve25519.edwardsPoint.EdwardsBasepointTable
import dev.sublab.curve25519.scalar.Scalar

/**
 * A pre-computed table of multiples of a Ristretto generator, for accelerating
 * fixed-base scalar multiplication.
 */
class RistrettoGeneratorTable(generator: RistrettoElement) {
    /**
     * Create a table of pre-computed multiples of generator.
     */
    val table = EdwardsBasepointTable(generator.representation)

    /**
     * Constant-time fixed-base scalar multiplication.
     *
     * @param s the Scalar to multiply by.
     * @return $[s]B$
     */
    fun multiply(s: Scalar) = RistrettoElement(table.multiply(s))
}