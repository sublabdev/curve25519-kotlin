package dev.sublab.curve25519.scalar.functions

import dev.sublab.curve25519.fieldElement.functions.load3
import dev.sublab.curve25519.fieldElement.functions.load4
import dev.sublab.curve25519.scalar.Scalar

private const val leftLong = 0x1FFFFF.toLong()

private fun Scalar.toLongArray() = longArrayOf(
    leftLong and load3(s, 0),
    leftLong and (load4(s, 2) shr 5),
    leftLong and (load3(s, 5) shr 2),
    leftLong and (load4(s, 7) shr 7),
    leftLong and (load4(s, 10) shr 4),
    leftLong and (load3(s, 13) shr 1),
    leftLong and (load4(s, 15) shr 6),
    leftLong and (load3(s, 18) shr 3),
    leftLong and load3(s, 21),
    leftLong and (load4(s, 23) shr 5),
    leftLong and (load3(s, 26) shr 2),
    load4(s, 28) shr 7
)

/**
 * Compute $a * b + c \bmod \ell$.
 *
 * @param b the Scalar to multiply this by.
 * @param c the Scalar to add to the product.
 * @return $a * b + c \bmod \ell$
 */
@Suppress("NAME_SHADOWING")
fun Scalar.multiplyAndAdd(b: Scalar, c: Scalar): Scalar {
    val a = toLongArray()
    val b = b.toLongArray()
    val c = c.toLongArray()

    val s = LongArray(24)
    val carry = LongArray(23)

    s[0] = c[0] + a[0] * b[0]
    s[1] = c[1] + a[0] * b[1] + a[1] * b[0]
    s[2] = c[2] + a[0] * b[2] + a[1] * b[1] + a[2] * b[0]
    s[3] = c[3] + a[0] * b[3] + a[1] * b[2] + a[2] * b[1] + a[3] * b[0]
    s[4] = c[4] + a[0] * b[4] + a[1] * b[3] + a[2] * b[2] + a[3] * b[1] + a[4] * b[0]
    s[5] = c[5] + a[0] * b[5] + a[1] * b[4] + a[2] * b[3] + a[3] * b[2] + a[4] * b[1] + a[5] * b[0]
    s[6] = c[6] + a[0] * b[6] + a[1] * b[5] + a[2] * b[4] + a[3] * b[3] + a[4] * b[2] + a[5] * b[1] + a[6] * b[0]
    s[7] = c[7] + a[0] * b[7] + a[1] * b[6] + a[2] * b[5] + a[3] * b[4] + a[4] * b[3] + a[5] * b[2] + a[6] * b[1] + a[7] * b[0]
    s[8] = c[8] + a[0] * b[8] + a[1] * b[7] + a[2] * b[6] + a[3] * b[5] + a[4] * b[4] + a[5] * b[3] + a[6] * b[2] + a[7] * b[1] + a[8] * b[0]
    s[9] = c[9] + a[0] * b[9] + a[1] * b[8] + a[2] * b[7] + a[3] * b[6] + a[4] * b[5] + a[5] * b[4] + a[6] * b[3] + a[7] * b[2] + a[8] * b[1] + a[9] * b[0]
    s[10] =
        c[10] + a[0] * b[10] + a[1] * b[9] + a[2] * b[8] + a[3] * b[7] + a[4] * b[6] + a[5] * b[5] + a[6] * b[4] + a[7] * b[3] + a[8] * b[2] + a[9] * b[1] + a[10] * b[0]
    s[11] =
        c[11] + a[0] * b[11] + a[1] * b[10] + a[2] * b[9] + a[3] * b[8] + a[4] * b[7] + a[5] * b[6] + a[6] * b[5] + a[7] * b[4] + a[8] * b[3] + a[9] * b[2] + a[10] * b[1] + a[11] * b[0]
    s[12] =
        a[1] * b[11] + a[2] * b[10] + a[3] * b[9] + a[4] * b[8] + a[5] * b[7] + a[6] * b[6] + a[7] * b[5] + a[8] * b[4] + a[9] * b[3] + a[10] * b[2] + a[11] * b[1]
    s[13] = a[2] * b[11] + a[3] * b[10] + a[4] * b[9] + a[5] * b[8] + a[6] * b[7] + a[7] * b[6] + a[8] * b[5] + a[9] * b[4] + a[10] * b[3] + a[11] * b[2]
    s[14] = a[3] * b[11] + a[4] * b[10] + a[5] * b[9] + a[6] * b[8] + a[7] * b[7] + a[8] * b[6] + a[9] * b[5] + a[10] * b[4] + a[11] * b[3]
    s[15] = a[4] * b[11] + a[5] * b[10] + a[6] * b[9] + a[7] * b[8] + a[8] * b[7] + a[9] * b[6] + a[10] * b[5] + a[11] * b[4]
    s[16] = a[5] * b[11] + a[6] * b[10] + a[7] * b[9] + a[8] * b[8] + a[9] * b[7] + a[10] * b[6] + a[11] * b[5]
    s[17] = a[6] * b[11] + a[7] * b[10] + a[8] * b[9] + a[9] * b[8] + a[10] * b[7] + a[11] * b[6]
    s[18] = a[7] * b[11] + a[8] * b[10] + a[9] * b[9] + a[10] * b[8] + a[11] * b[7]
    s[19] = a[8] * b[11] + a[9] * b[10] + a[10] * b[9] + a[11] * b[8]
    s[20] = a[9] * b[11] + a[10] * b[10] + a[11] * b[9]
    s[21] = a[10] * b[11] + a[11] * b[10]
    s[22] = a[11] * b[11]
    carry[0] = s[0] + (1 shl 20) shr 21
    s[1] += carry[0]
    s[0] -= carry[0] shl 21
    carry[2] = s[2] + (1 shl 20) shr 21
    s[3] += carry[2]
    s[2] -= carry[2] shl 21
    carry[4] = s[4] + (1 shl 20) shr 21
    s[5] += carry[4]
    s[4] -= carry[4] shl 21
    carry[6] = s[6] + (1 shl 20) shr 21
    s[7] += carry[6]
    s[6] -= carry[6] shl 21
    carry[8] = s[8] + (1 shl 20) shr 21
    s[9] += carry[8]
    s[8] -= carry[8] shl 21
    carry[10] = s[10] + (1 shl 20) shr 21
    s[11] += carry[10]
    s[10] -= carry[10] shl 21
    carry[12] = s[12] + (1 shl 20) shr 21
    s[13] += carry[12]
    s[12] -= carry[12] shl 21
    carry[14] = s[14] + (1 shl 20) shr 21
    s[15] += carry[14]
    s[14] -= carry[14] shl 21
    carry[16] = s[16] + (1 shl 20) shr 21
    s[17] += carry[16]
    s[16] -= carry[16] shl 21
    carry[18] = s[18] + (1 shl 20) shr 21
    s[19] += carry[18]
    s[18] -= carry[18] shl 21
    carry[20] = s[20] + (1 shl 20) shr 21
    s[21] += carry[20]
    s[20] -= carry[20] shl 21
    carry[22] = s[22] + (1 shl 20) shr 21
    s[23] = carry[22]
    s[22] -= carry[22] shl 21
    carry[1] = s[1] + (1 shl 20) shr 21
    s[2] += carry[1]
    s[1] -= carry[1] shl 21
    carry[3] = s[3] + (1 shl 20) shr 21
    s[4] += carry[3]
    s[3] -= carry[3] shl 21
    carry[5] = s[5] + (1 shl 20) shr 21
    s[6] += carry[5]
    s[5] -= carry[5] shl 21
    carry[7] = s[7] + (1 shl 20) shr 21
    s[8] += carry[7]
    s[7] -= carry[7] shl 21
    carry[9] = s[9] + (1 shl 20) shr 21
    s[10] += carry[9]
    s[9] -= carry[9] shl 21
    carry[11] = s[11] + (1 shl 20) shr 21
    s[12] += carry[11]
    s[11] -= carry[11] shl 21
    carry[13] = s[13] + (1 shl 20) shr 21
    s[14] += carry[13]
    s[13] -= carry[13] shl 21
    carry[15] = s[15] + (1 shl 20) shr 21
    s[16] += carry[15]
    s[15] -= carry[15] shl 21
    carry[17] = s[17] + (1 shl 20) shr 21
    s[18] += carry[17]
    s[17] -= carry[17] shl 21
    carry[19] = s[19] + (1 shl 20) shr 21
    s[20] += carry[19]
    s[19] -= carry[19] shl 21
    carry[21] = s[21] + (1 shl 20) shr 21
    s[22] += carry[21]
    s[21] -= carry[21] shl 21

    return op1(s, carry)
}