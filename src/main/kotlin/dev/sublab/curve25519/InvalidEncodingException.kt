package dev.sublab.curve25519

/**
 * Thrown to indicate that a [dev.sublab.curve25519.edwardsPoint.CompressedEdwardsY] or
 * [dev.sublab.curve25519.ristrettoElement.CompressedRistretto] was an invalid encoding of an
 * [dev.sublab.curve25519.edwardsPoint.EdwardsPoint] or [dev.sublab.curve25519.ristrettoElement.RistrettoElement].
 */
class InvalidEncodingException internal constructor(msg: String) : Exception(msg)