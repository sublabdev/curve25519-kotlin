package dev.sublab.curve25519.unpackedScalar

import dev.sublab.curve25519.scalar.Scalar
import dev.sublab.curve25519.unpackedScalar.functions.montgomeryReduce
import dev.sublab.curve25519.unpackedScalar.functions.toUnpackedScalar

internal val ZERO get() = UnpackedScalar(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0))
internal const val MASK_29_BITS = (1 shl 29) - 1
internal const val MASK_24_BITS = (1 shl 24) - 1

internal fun m(a: Int, b: Int) = (a.toLong()) * (b.toLong())

/**
 * Represents an element in ℤ/lℤ as 9 29-bit limbs.
 */
internal class UnpackedScalar(internal val s: IntArray) {
    init {
        if (s.size != 9) {
            throw IllegalArgumentException("Invalid radix-2^29 representation")
        }
    }

    /**
     * Compute $a + b \bmod \ell$.
     *
     * @param b the Scalar to add to this.
     * @return $a + b \bmod \ell$
     */
    fun add(b: UnpackedScalar): UnpackedScalar {
        val sum = IntArray(9)
        var carry = 0
        for (i in 0 .. 8) {
            carry = s[i] + b.s[i] + (carry shr 29)
            sum[i] = carry and MASK_29_BITS
        }

        // Subtract l if the sum is >= l
        return UnpackedScalar(sum).subtract(L)
    }
    /**
     * Compute $a - b \bmod \ell$.
     *
     * @param b the Scalar to subtract from this.
     * @return $a - b \bmod \ell$
     */
    fun subtract(b: UnpackedScalar): UnpackedScalar {
        val difference = IntArray(9)
        var  borrow = 0
        for (i in 0 .. 8) {
            borrow = this.s[i] - (b.s[i] + (borrow ushr 31))
            difference[i] = borrow and MASK_29_BITS
        }

        // Conditionally add l if the difference is negative
        val underflowMask = ((borrow ushr 31) xor 1) - 1
        var carry = 0
        for (i in 0 .. 8) {
            carry = (carry ushr 29) + difference[i] + (L.s[i] and underflowMask)
            difference[i] = carry and MASK_29_BITS
        }

        return UnpackedScalar(difference)
    }
    /**
     * Compute $a * b \bmod \ell$.
     *
     * @param value the Scalar to multiply with this.
     * @return the unreduced limbs.
     */
    internal fun mulInternal(value: UnpackedScalar): LongArray {
        val a = s
        val b = value.s
        val z = LongArray(17)

        // @formatter:off
        z[0] = m(a[0], b[0]) // c00
        z[1] = m(a[0], b[1]) + m(a[1], b[0]) // c01
        z[2] = m(a[0], b[2]) + m(a[1], b[1]) + m(a[2], b[0]) // c02
        z[3] = m(a[0], b[3]) + m(a[1], b[2]) + m(a[2], b[1]) + m(a[3], b[0]) // c03
        z[4] = m(a[0], b[4]) + m(a[1], b[3]) + m(a[2], b[2]) + m(a[3], b[1]) + m(a[4], b[0]) // c04
        z[5] = m(a[1], b[4]) + m(a[2], b[3]) + m(a[3], b[2]) + m(a[4], b[1]) // c05
        z[6] = m(a[2], b[4]) + m(a[3], b[3]) + m(a[4], b[2]) // c06
        z[7] = m(a[3], b[4]) + m(a[4], b[3]) // c07
        z[8] = (m(a[4], b[4])) - z[3] // c08 - c03
        z[10] = z[5] - (m(a[5], b[5])) // c05mc10
        z[11] = z[6] - (m(a[5], b[6]) + m(a[6], b[5])) // c06mc11
        z[12] = z[7] - (m(a[5], b[7]) + m(a[6], b[6]) + m(a[7], b[5])) // c07mc12
        z[13] = m(a[5], b[8]) + m(a[6], b[7]) + m(a[7], b[6]) + m(a[8], b[5]) // c13
        z[14] = m(a[6], b[8]) + m(a[7], b[7]) + m(a[8], b[6]) // c14
        z[15] = m(a[7], b[8]) + m(a[8], b[7]) // c15
        z[16] = m(a[8], b[8]) // c16
        z[5] = z[10] - (z[0]) // c05mc10 - c00
        z[6] = z[11] - (z[1]) // c06mc11 - c01
        z[7] = z[12] - (z[2]) // c07mc12 - c02
        z[8] = z[8] - (z[13]) // c08mc13 - c03
        z[9] = z[14] + (z[4]) // c14 + c04
        z[10] = z[15] + (z[10]) // c15 + c05mc10
        z[11] = z[16] + (z[11]) // c16 + c06mc11

        val aa = intArrayOf(
            a[0] + a[5],
            a[1] + a[6],
            a[2] + a[7],
            a[3] + a[8]
        )

        val bb = intArrayOf(
            b[0] + b[5],
            b[1] + b[6],
            b[2] + b[7],
            b[3] + b[8]
        )

        z[5] = (m(aa[0], bb[0])) + z[5] // c20 + c05mc10 - c00
        z[6] = (m(aa[0], bb[1]) + m(aa[1], bb[0])) + z[6] // c21 + c06mc11 - c01
        z[7] = (m(aa[0], bb[2]) + m(aa[1], bb[1]) + m(aa[2], bb[0])) + z[7] // c22 + c07mc12 - c02
        z[8] = (m(aa[0], bb[3]) + m(aa[1], bb[2]) + m(aa[2], bb[1]) + m(aa[3], bb[0])) + z[8] // c23 + c08mc13 - c03
        z[9] = (m(aa[0], b[4]) + m(aa[1], bb[3]) + m(aa[2], bb[2]) + m(aa[3], bb[1]) + m(a[4], bb[0])) - z[9] // c24 - c14 - c04
        z[10] = (m(aa[1], b[4]) + m(aa[2], bb[3]) + m(aa[3], bb[2]) + m(a[4], bb[1])) - z[10] // c25 - c15 - c05mc10
        z[11] = (m(aa[2], b[4]) + m(aa[3], bb[3]) + m(a[4], bb[2])) - z[11] // c26 - c16 - c06mc11
        z[12] = (m(aa[3], b[4]) + m(a[4], bb[3])) - z[12] // c27 - c07mc12

        // @formatter:on
        return z
    }

    /**
     * Compute $a * b \bmod \ell$.
     *
     * @param b the Scalar to multiply with this.
     * @return $a * b \bmod \ell$
     */
    fun multiply(b: UnpackedScalar) = montgomeryReduce(montgomeryReduce(mulInternal(b)).mulInternal(RR))

    companion object {
        /**
         * The order of the Ed25519 basepoint, $\ell = 2^{252} +
         * 27742317777372353535851937790883648493$.
         */
        val BASEPOINT_ORDER = Scalar(
            byteArrayOf(
                0xed.toByte(), 0xd3.toByte(), 0xf5.toByte(),
                0x5c, 0x1a, 0x63, 0x12, 0x58,
                0xd6.toByte(), 0x9c.toByte(), 0xf7.toByte(), 0xa2.toByte(), 0xde.toByte(), 0xf9.toByte(), 0xde.toByte(),
                0x14, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x10
            )
        )

        /**
         * The unpacked form of the Ed25519 basepoint order $\ell$.
         */
        val L = BASEPOINT_ORDER.toByteArray().toUnpackedScalar()

        /**
         * $\ell * \text{LFACTOR} = -1 \bmod 2^{29}$
         */
        val LFACTOR: Int = 0x12547e1b

        /**
         * $= R \bmod \ell$ where $R = 2^{261}$
         */
        val R = UnpackedScalar(
            intArrayOf(
                0x114df9ed, 0x1a617303, 0x0f7c098c, 0x16793167,
                0x1ffd656e, 0x1fffffff, 0x1fffffff, 0x1fffffff, 0x000fffff
            )
        )

        /**
         * $= R^2 \bmod \ell$ where $R = 2^{261}$
         */
        val RR = UnpackedScalar(
            intArrayOf(
                0x0b5f9d12, 0x1e141b17, 0x158d7f3d, 0x143f3757,
                0x1972d781, 0x042feb7c, 0x1ceec73d, 0x1e184d1e, 0x0005046d
            )
        )
    }
}