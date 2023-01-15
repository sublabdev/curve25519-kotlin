/**
 *
 * Copyright 2023 SUBSTRATE LABORATORY LLC <info@sublab.dev>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package dev.sublab.curve25519.scalar

import dev.sublab.curve25519.scalar.functions.multiplyAndAdd
import dev.sublab.curve25519.scalar.functions.reduceToScalar
import dev.sublab.curve25519.unpackedScalar.functions.toByteArray
import dev.sublab.curve25519.unpackedScalar.functions.toUnpackedScalar
import dev.sublab.hex.hex
import dev.sublab.support.ConstantTime

/**
 * An integer $s \lt 2^{255}$ which represents an element of the field
 * $\mathbb{Z} / \ell$.
 */
class Scalar internal constructor(s: ByteArray) {
    /**
     * The 32-byte little-endian encoding of an integer representing a scalar modulo
     * the group order.
     *
     *
     * Invariant: the highest bit must be zero ($s[31] \le 127$).
     */
    internal val s = s.copyOf()

    init {
        require(s.size == 32 && ( (s[31].toInt() shr 7) and 0x01 == 0 ) ) { "Invalid scalar representation" }
    }

    /**
     * Convert this Scalar to its underlying sequence of bytes.
     *
     * @return the 32-byte little-endian encoding of this Scalar.
     */
    fun toByteArray() = s.copyOf()

    /**
     * Constant-time equality check.
     *
     *
     * Compares the encodings of the two Scalars.
     *
     * @return 1 if self and other are equal, 0 otherwise.
     */
    fun ctEquals(other: Scalar) = ConstantTime.equal(s, other.s)

    /**
     * Equality check overridden to be constant-time.
     *
     *
     * Fails fast if the objects are of different types.
     *
     * @return true if self and other are equal, false otherwise.
     */
    override fun equals(other: Any?): Boolean {
        if (other !is Scalar) {
            return false
        }

        return ctEquals(other)
    }

    override fun hashCode() = s.contentHashCode()
    override fun toString() = "Scalar(${s.hex.encode()})"

    /**
     * Check whether this Scalar is the canonical representative mod $\ell$.
     */
    val isCanonical: Boolean
        get() = ctEquals(reduce())

    /**
     * Reduce this Scalar modulo $\ell$.
     *
     * @return the reduced Scalar.
     */
    fun reduce() = s.reduceToScalar()

    /**
     * Compute $a + b \bmod \ell$.
     *
     *
     * If $a$ and $b$ are both canonical Scalars, the result is guaranteed to be a
     * canonical Scalar. In all other cases, the result may be outside the range
     * $[0, \ell)$.
     *
     * @param b the Scalar to add to this.
     * @return $a + b \bmod \ell$
     */
    fun add(b: Scalar) = Scalar(s.toUnpackedScalar().add(b.s.toUnpackedScalar()).toByteArray())

    /**
     * Compute $a - b \bmod \ell$.
     *
     *
     * If $a$ and $b$ are both canonical Scalars, the result is guaranteed to be a
     * canonical Scalar. In all other cases, the result may be outside the range
     * $[0, \ell)$.
     *
     * @param b the Scalar to subtract from this.
     * @return $a - b \bmod \ell$
     */
    fun subtract(b: Scalar) = Scalar(s.toUnpackedScalar().subtract(b.s.toUnpackedScalar()).toByteArray())

    /**
     * Compute $a * b \bmod \ell$.
     *
     * @param b the Scalar to multiply with this.
     * @return $a * b \bmod \ell$
     */
    fun multiply(b: Scalar) = multiplyAndAdd(b, ZERO)

    companion object {
        val ZERO = Scalar(
            byteArrayOf(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
            )
        )

        val ONE = Scalar(
            byteArrayOf(
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
            )
        )
    }
}