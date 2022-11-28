package dev.sublab.curve25519.scalar.functions

import dev.sublab.curve25519.scalar.Scalar


/**
 * Attempt to construct a Scalar from a canonical byte representation.
 *
 * @return the Scalar if the input was its canonical representation.
 */
fun ByteArray.toScalarCanonicalBytes(): Scalar {
    val s = Scalar(this)
    require(s.isCanonical) { "Non-canonical scalar representation" }
    return s
}