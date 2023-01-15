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

import dev.sublab.curve25519.InvalidEncodingException
import dev.sublab.curve25519.edwardsPoint.EdwardsPoint
import dev.sublab.curve25519.fieldElement.FieldElement
import dev.sublab.curve25519.fieldElement.functions.multiply
import dev.sublab.curve25519.fieldElement.functions.square
import dev.sublab.curve25519.fieldElement.functions.toByteArray
import dev.sublab.curve25519.fieldElement.functions.toFieldElement
import dev.sublab.curve25519.fieldElement.sqrtRatioM1
import dev.sublab.support.ConstantTime

/**
 * A Ristretto element in compressed wire format.
 *
 *
 * The Ristretto encoding is canonical, so two elements are equal if and only if
 * their encodings are equal.
 */
class CompressedRistretto(private val data: ByteArray) {
    init {
        require(data.size == 32) { "Invalid CompressedRistretto encoding" }
    }

    /**
     * Attempts to decompress to a RistrettoElement.
     *
     *
     * This is the ristretto255 DECODE function.
     *
     * @return a RistrettoElement, if this is the canonical encoding of an element
     * of the ristretto255 group.
     * @throws InvalidEncodingException if this is an invalid encoding.
     */
    @Throws(InvalidEncodingException::class)
    fun decompress(): RistrettoElement {
        decompressedRepresentation?.let {
            return RistrettoElement(it)
        }

        // 1. First, interpret the string as an integer s in little-endian
        // representation. If the resulting value is >= p, decoding fails.
        // 2. If IS_NEGATIVE(s) returns TRUE, decoding fails.
        val s = data.toFieldElement()
        val sBytes: ByteArray = s.toByteArray()
        val sIsCanonical = ConstantTime.equal(data, sBytes)
        if (!sIsCanonical || s.isNegative) {
            throw InvalidEncodingException("Invalid ristretto255 encoding")
        }

        // 3. Process s as follows:
        val ss = s.square()
        val u1 = FieldElement.ONE.subtract(ss)
        val u2 = FieldElement.ONE.add(ss)
        val u2Sqr = u2.square()
        val v = FieldElement.NEG_EDWARDS_D.multiply(u1.square()).subtract(u2Sqr)
        val invsqrt = sqrtRatioM1(FieldElement.ONE, v.multiply(u2Sqr))
        val denX = invsqrt.result.multiply(u2)
        val denY = invsqrt.result.multiply(denX).multiply(v)
        val x = s.add(s).multiply(denX).ctAbs()
        val y = u1.multiply(denY)
        val t = x.multiply(y)

        // 4. If was_square is FALSE, or IS_NEGATIVE(t) returns TRUE, or y = 0, decoding
        // fails. Otherwise, return the internal representation in extended coordinates
        // (x, y, 1, t).
        if (!invsqrt.wasSquare || t.isNegative || y.isZero) {
            throw InvalidEncodingException("Invalid ristretto255 encoding")
        }

        val p = EdwardsPoint(x, y, FieldElement.ONE, t)
        decompressedRepresentation = p
        return RistrettoElement(p)
    }

    private var decompressedRepresentation: EdwardsPoint? = null

    /**
     * Encode the element to its compressed 32-byte form.
     *
     * @return the encoded element.
     */
    fun toByteArray() = data.copyOf()

    /**
     * Constant-time equality check.
     *
     * @return 1 if this and other are equal, 0 otherwise.
     */
    fun ctEquals(other: CompressedRistretto) = ConstantTime.equal(data, other.data)

    /**
     * Equality check overridden to be constant-time.
     *
     *
     * Fails fast if the objects are of different types.
     *
     * @return true if this and other are equal, false otherwise.
     */
    override fun equals(other: Any?): Boolean {
        if (other !is CompressedRistretto) {
            return false
        }

        return ctEquals(other)
    }

    override fun hashCode() = data.contentHashCode()
}