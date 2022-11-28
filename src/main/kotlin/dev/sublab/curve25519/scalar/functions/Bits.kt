package dev.sublab.curve25519.scalar.functions

import dev.sublab.curve25519.scalar.Scalar

/**
 * Construct a Scalar from the low 255 bits of a 256-bit integer.
 *
 *
 * This function is intended for applications like X25519 which require specific
 * bit-patterns when performing scalar multiplication.
 */
fun ByteArray.toScalarFromBits() = Scalar(copyOf().apply {
    // Ensure that s < 2^255 by masking the high bit
    this[31] = (this[31].toInt() and 0x7f).toByte()
})