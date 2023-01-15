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

package dev.sublab.curve25519.ristrettoElement

import dev.sublab.curve25519.edwardsPoint.EdwardsPoint
import dev.sublab.curve25519.fieldElement.FieldElement
import dev.sublab.curve25519.fieldElement.functions.multiply
import dev.sublab.curve25519.fieldElement.functions.square
import dev.sublab.curve25519.fieldElement.functions.toByteArray
import dev.sublab.curve25519.fieldElement.sqrtRatioM1
import dev.sublab.curve25519.scalar.Scalar

/**
 * An element of the prime-order ristretto255 group.
 */
@Suppress("unused")
class RistrettoElement internal constructor(
    /**
     * The internal representation. Not canonical.
     */
    internal val representation: EdwardsPoint
) {
    /**
     * Compress this element using the Ristretto encoding.
     *
     *
     * This is the ristretto255 ENCODE function.
     *
     * @return the encoded element.
     */
    fun compress(): CompressedRistretto {
        // 1. Process the internal representation into a field element s as follows:
        val u1 = representation.z.add(representation.y).multiply(representation.z.subtract(representation.y))
        val u2 = representation.x.multiply(representation.y)

        // Ignore was_square since this is always square
        val invsqrt = sqrtRatioM1(FieldElement.ONE, u1.multiply(u2.square()))
        val den1 = invsqrt.result.multiply(u1)
        val den2 = invsqrt.result.multiply(u2)
        val zInv = den1.multiply(den2).multiply(representation.t)
        val ix = representation.x.multiply(FieldElement.SQRT_M1)
        val iy = representation.y.multiply(FieldElement.SQRT_M1)
        val enchantedDenominator = den1.multiply(FieldElement.INVSQRT_A_MINUS_D)
        val rotate = representation.t.multiply(zInv).isNegative
        val x = representation.x.ctSelect(iy, rotate)
        var y = representation.y.ctSelect(ix, rotate)
        val z = representation.z
        val denInv = den2.ctSelect(enchantedDenominator, rotate)
        y = y.ctSelect(y.negate(), x.multiply(zInv).isNegative)
        var s = denInv.multiply(z.subtract(y))
        s = s.ctSelect(s.negate(), s.isNegative)

        // 2. Return the canonical little-endian encoding of s.
        return CompressedRistretto(s.toByteArray())
    }

    /**
     * Constant-time equality check.
     *
     *
     * This is the ristretto255 EQUALS function.
     *
     * @return true if this and other are equal, false otherwise.
     */
    fun ctEquals(other: RistrettoElement): Boolean {
        val x1y2 = representation.x.multiply(other.representation.y)
        val y1x2 = representation.y.multiply(other.representation.x)
        val y1y2 = representation.y.multiply(other.representation.y)
        val x1x2 = representation.x.multiply(other.representation.x)
        return x1y2.ctEquals(y1x2) || y1y2.ctEquals(x1x2)
    }

    /**
     * Constant-time selection between two RistrettoElements.
     *
     * @param that the other element.
     * @param b    must be 0 or 1, otherwise results are undefined.
     * @return a copy of this if $b == 0$, or a copy of that if $b == 1$.
     */
    fun ctSelect(that: RistrettoElement, b: Int) = RistrettoElement(representation.ctSelect(that.representation, b))

    fun ctSelect(that: RistrettoElement, b: Boolean) = ctSelect(that, if (b) 1 else 0)

    /**
     * Equality check overridden to be constant-time.
     *
     *
     * Fails fast if the objects are of different types.
     *
     * @return true if this and other are equal, false otherwise.
     */
    override fun equals(other: Any?): Boolean {
        if (other !is RistrettoElement) {
            return false
        }

        return ctEquals(other)
    }

    override fun hashCode(): Int {
        // The general contract for the hashCode method states that equal objects must
        // have equal hash codes. Object equality is based on the encodings of the
        // elements, not their internal representations (which are not canonical). Note
        // that equality is actually implemented using the ristretto255 EQUALS function,
        // but it is simpler to derive a hashCode from the element's encoding.
        return compress().hashCode()
    }

    /**
     * Group addition.
     *
     * @param q the element to add to this one.
     * @return $P + Q$
     */
    fun add(q: RistrettoElement) = RistrettoElement(representation.add(q.representation))

    /**
     * Group subtraction.
     *
     * @param Q the element to subtract from this one.
     * @return $P - Q$
     */
    fun subtract(q: RistrettoElement) = RistrettoElement(representation.subtract(q.representation))

    /**
     * Element negation.
     *
     * @return $-P$
     */
    fun negate() = RistrettoElement(representation.negate())

    /**
     * Element doubling.
     *
     * @return $[2]P$
     */
    fun double() = RistrettoElement(representation.double())

    /**
     * Constant-time variable-base scalar multiplication.
     *
     * @param s the Scalar to multiply by.
     * @return $[s]P$
     */
    fun multiply(s: Scalar) = RistrettoElement(representation.multiply(s))

    override fun toString() = "RistrettoElement($representation)"

    companion object {
        val IDENTITY = RistrettoElement(EdwardsPoint.IDENTITY)

        /**
         * The ristretto255 generator, as a RistrettoElement.
         */
        val RISTRETTO_GENERATOR = RistrettoElement(EdwardsPoint.ED25519_BASEPOINT)

        /**
         * Table containing pre-computed multiples of the ristretto255 generator.
         */
        val RISTRETTO_GENERATOR_TABLE = RistrettoGeneratorTable(RISTRETTO_GENERATOR)
    }
}
