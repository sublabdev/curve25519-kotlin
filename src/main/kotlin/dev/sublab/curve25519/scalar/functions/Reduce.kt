package dev.sublab.curve25519.scalar.functions

import dev.sublab.curve25519.scalar.Scalar
import dev.sublab.curve25519.unpackedScalar.UnpackedScalar.Companion.R
import dev.sublab.curve25519.unpackedScalar.functions.montgomeryReduce
import dev.sublab.curve25519.unpackedScalar.functions.toByteArray
import dev.sublab.curve25519.unpackedScalar.functions.toUnpackedScalar

/**
 * Reduce the given scalar modulo $\ell$.
 *
 * @return the reduced Scalar.
 */
internal fun ByteArray.reduceToScalar()
    = Scalar(montgomeryReduce(toUnpackedScalar().mulInternal(R)).toByteArray())
