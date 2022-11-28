package dev.sublab.curve25519.fieldElement

import dev.sublab.curve25519.fieldElement.functions.square
import dev.sublab.curve25519.fieldElement.functions.toByteArray
import dev.sublab.support.ConstantTime

/**
 * A field element of the field $\mathbb{Z} / (2^{255} - 19)$.
 */
internal class FieldElement(
    /**
     * An element $t$, entries $t[0] \dots t[9]$, represents the integer $t[0] +
     * 2^{26} t[1] + 2^{51} t[2] + 2^{77} t[3] + 2^{102} t[4] + \dots + 2^{230}
     * t[9]$. Bounds on each $t[i]$ vary depending on context.
     */
    internal val t: IntArray
) {
    init {
        require(t.size == 10) { "Invalid radix-2^25.5 representation" }
    }

    /**
     * Constant-time equality check.
     *
     *
     * Compares the encodings of the two FieldElements.
     *
     * @return true if self and other are equal, false otherwise.
     */
    fun ctEquals(other: FieldElement) = ConstantTime.equal(toByteArray(), other.toByteArray())

    /**
     * Constant-time selection between two FieldElements.
     *
     *
     * Implemented as a conditional copy. Logic is inspired by the SUPERCOP
     * implementation.
     *
     * @param that the other field element.
     * @param b    must be 0 or 1, otherwise results are undefined.
     * @return a copy of this if $b == 0$, or a copy of val if $b == 1$.
     * @see [SUPERCOP](https://github.com/floodyberry/supercop/blob/master/crypto_sign/ed25519/ref10/fe_cmov.c)
     */
    @Suppress("NAME_SHADOWING")
    fun ctSelect(that: FieldElement, b: Int): FieldElement {
        var b = b
        b = -b

        val result = IntArray(10)
        for (i in 0..9) {
            result[i] = t[i]
            var x = t[i] xor that.t[i]
            x = x and b
            result[i] = result[i] xor x
        }

        return FieldElement(result)
    }

    fun ctSelect(that: FieldElement, b: Boolean) = ctSelect(that, if (b) 1 else 0)

    /**
     * Compute the absolute value of this FieldElement in constant time.
     *
     * @return $|\text{this}|$.
     */
    fun ctAbs() = ctSelect(negate(), isNegative)

    /**
     * Equality check overridden to be constant-time.
     *
     *
     * Fails fast if the objects are of different types.
     *
     * @return true if self and other are equal, false otherwise.
     */
    override fun equals(other: Any?): Boolean {
        if (other !is FieldElement) {
            return false
        }

        return ctEquals(other)
    }

    /**
     * The general contract for the hashCode method states that equal objects must
     * have equal hash codes. Object equality is based on the encodings of the
     * field elements, not their internal representations (which may not be canonical).
     */
    override fun hashCode() = toByteArray().contentHashCode()

    /**
     * Determine whether this FieldElement is zero.
     *
     * @return true if this FieldElement is zero, false otherwise.
     */
    val isZero: Boolean
        get() = ConstantTime.equal(toByteArray(), ZERO_BYTES)

    /**
     * Determine whether this FieldElement is negative.
     *
     *
     * As in RFC 8032, a FieldElement is negative if the least significant bit of
     * the encoding is 1.
     *
     * @return true if this FieldElement is negative, false otherwise.
     * @see [RFC 8032](https://tools.ietf.org/html/rfc8032)
     */
    val isNegative: Boolean
        get() = (toByteArray()[0].toInt() and 1) == 1

    /**
     * $h = f + g$
     *
     *
     * Preconditions:
     *
     *  * $|f|$ bounded by $1.1*2^{25},1.1*2^{24},1.1*2^{25},1.1*2^{24},$ etc.
     *  * $|g|$ bounded by $1.1*2^{25},1.1*2^{24},1.1*2^{25},1.1*2^{24},$ etc.
     *
     *
     * Postconditions:
     *
     *  * $|h|$ bounded by $1.1*2^{26},1.1*2^{25},1.1*2^{26},1.1*2^{25},$ etc.
     *
     * @param value The field element to add.
     * @return The field element this + val.
     */
    fun add(value: FieldElement): FieldElement {
        val g = value.t
        val h = IntArray(10)

        for (i in 0..9) {
            h[i] = t[i] + g[i]
        }

        return FieldElement(h)
    }

    /**
     * $h = f - g$
     *
     *
     * Can overlap $h$ with $f$ or $g$.
     *
     *
     * Preconditions:
     *
     *  * $|f|$ bounded by $1.1*2^{25},1.1*2^{24},1.1*2^{25},1.1*2^{24},$ etc.
     *  * $|g|$ bounded by $1.1*2^{25},1.1*2^{24},1.1*2^{25},1.1*2^{24},$ etc.
     *
     *
     * Postconditions:
     *
     *  * $|h|$ bounded by $1.1*2^{26},1.1*2^{25},1.1*2^{26},1.1*2^{25},$ etc.
     *
     * @param value The field element to subtract.
     * @return The field element this - val.
     */
    fun subtract(value: FieldElement): FieldElement {
        val g = value.t
        val h = IntArray(10)

        for (i in 0..9) {
            h[i] = t[i] - g[i]
        }

        return FieldElement(h)
    }

    /**
     * $h = -f$
     *
     *
     * Preconditions:
     *
     *  * $|f|$ bounded by $1.1*2^{25},1.1*2^{24},1.1*2^{25},1.1*2^{24},$ etc.
     *
     *
     * Postconditions:
     *
     *  * $|h|$ bounded by $1.1*2^{25},1.1*2^{24},1.1*2^{25},1.1*2^{24},$ etc.
     *
     * @return The field element (-1) * this.
     */
    fun negate(): FieldElement {
        val h = IntArray(10)
        for (i in 0..9) {
            h[i] = -t[i]
        }

        return FieldElement(h)
    }

    override fun toString() = "FieldElement([${t.joinToString(", ") { it.toString() }}])"

    companion object {
        val ZERO = FieldElement(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
        val ONE = FieldElement(intArrayOf(1, 0, 0, 0, 0, 0, 0, 0, 0, 0))
        val MINUS_ONE = ZERO.subtract(ONE)
        val ZERO_BYTES = ByteArray(32)

        /**
         * Edwards $d$ value, equal to $-121665/121666 \bmod p$.
         */
        val EDWARDS_D = FieldElement(
            intArrayOf(
                -10913610, 13857413, -15372611, 6949391, 114729,
                -8787816, -6275908, -3247719, -18696448, -12055116
            )
        )

        /**
         * Edwards $-d$ value, equal to $121665/121666 \bmod p$.
         */
        val NEG_EDWARDS_D = EDWARDS_D.negate()

        /**
         * Edwards $2*d$ value, equal to $2*(-121665/121666) \bmod p$.
         */
        val EDWARDS_2D = FieldElement(
            intArrayOf( // @formatter:off
                -21827239, -5839606, -30745221, 13898782, 229458,
                15978800, -12551817, -6495438, 29715968, 9444199
            )
        )

        /**
         * $= 1 - d^2$, where $d$ is the Edwards curve parameter.
         */
        val ONE_MINUS_D_SQ = ONE.subtract(EDWARDS_D.square())

        /**
         * $= (d - 1)^2$, where $d$ is the Edwards curve parameter.
         */
        val D_MINUS_ONE_SQ = EDWARDS_D.subtract(ONE).square()

        /**
         * $= \sqrt{a*d - 1}$, where $a = -1 \bmod p$, $d$ are the Edwards curve
         * parameters.
         */
        val SQRT_AD_MINUS_ONE = FieldElement(
            intArrayOf( // @formatter:off
                24849947, -153582, -23613485, 6347715, -21072328,
                -667138, -25271143, -15367704, -870347, 14525639
            )
        )

        /**
         * $= 1/\sqrt{a-d}$, where $a = -1 \bmod p$, $d$ are the Edwards curve
         * parameters.
         */
        val INVSQRT_A_MINUS_D = FieldElement(
            intArrayOf( // @formatter:off
                6111485, 4156064, -27798727, 12243468, -25904040,
                120897, 20826367, -7060776, 6093568, -1986012
            )
        )

        /**
         * Precomputed value of one of the square roots of -1 (mod p).
         */
        val SQRT_M1 = FieldElement(
            intArrayOf(
                -32595792, -7943725, 9377950, 3500415, 12389472,
                -272473, -25146209, -2005654, 326686, 11406482
            )
        )
    }
}