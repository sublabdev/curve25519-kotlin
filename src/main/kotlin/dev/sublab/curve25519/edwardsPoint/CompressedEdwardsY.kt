package dev.sublab.curve25519.edwardsPoint

import dev.sublab.curve25519.InvalidEncodingException
import dev.sublab.curve25519.fieldElement.FieldElement
import dev.sublab.curve25519.fieldElement.functions.multiply
import dev.sublab.curve25519.fieldElement.functions.square
import dev.sublab.curve25519.fieldElement.functions.toFieldElement
import dev.sublab.curve25519.fieldElement.sqrtRatioM1
import dev.sublab.support.ConstantTime

/**
 * An Edwards point encoded in "Edwards y" / "Ed25519" format.
 *
 *
 * In "Edwards y" / "Ed25519" format, the curve point $(x, y)$ is determined by
 * the $y$-coordinate and the sign of $x$.
 *
 *
 * The first 255 bits of a CompressedEdwardsY represent the $y$-coordinate. The
 * high bit of the 32nd byte represents the sign of $x$.
 */
class CompressedEdwardsY(private val data: ByteArray) {
    init {
        require(data.size == 32) { "Invalid CompressedEdwardsY encoding" }
    }

    /**
     * Attempts to decompress to an EdwardsPoint.
     *
     * @return an EdwardsPoint, if this is a valid encoding.
     * @throws InvalidEncodingException if this is an invalid encoding.
     */
    @Throws(InvalidEncodingException::class)
    fun decompress(): EdwardsPoint {
        val y = data.toFieldElement()
        val yy = y.square()

        // u = y²-1
        val u = yy.subtract(FieldElement.ONE)

        // v = dy²+1
        val v = yy.multiply(FieldElement.EDWARDS_D).add(FieldElement.ONE)
        val sqrt = sqrtRatioM1(u, v)
        if (!sqrt.wasSquare) {
            throw InvalidEncodingException("not a valid EdwardsPoint")
        }

        val x = sqrt.result.negate().ctSelect(
            sqrt.result,
            ConstantTime.equal(sqrt.result.isNegative, ConstantTime.bit(data, 255))
        )

        return EdwardsPoint(x, y, FieldElement.ONE, x.multiply(y))
    }

    /**
     * Encode the point to its compressed 32-byte form.
     *
     * @return the encoded point.
     */
    fun toByteArray() = data.copyOf()

    /**
     * Constant-time equality check.
     *
     * @return 1 if this and other are equal, 0 otherwise.
     */
    fun ctEquals(other: CompressedEdwardsY) = ConstantTime.equal(data, other.data)

    /**
     * Equality check overridden to be constant-time.
     *
     *
     * Fails fast if the objects are of different types.
     *
     * @return true if this and other are equal, false otherwise.
     */
    override fun equals(other: Any?): Boolean {
        if (other !is CompressedEdwardsY) {
            return false
        }

        return ctEquals(other)
    }

    override fun hashCode() = data.contentHashCode()
}