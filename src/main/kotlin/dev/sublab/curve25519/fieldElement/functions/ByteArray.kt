package dev.sublab.curve25519.fieldElement.functions

import dev.sublab.curve25519.fieldElement.FieldElement

/**
 * Encode a FieldElement in its 32-byte representation.
 *
 *
 * This is done in two steps:
 *
 *  1. Reduce the value of the field element modulo $p$.
 *  1. Convert the field element to the 32 byte representation.
 *
 *
 * The idea for the modulo $p$ reduction algorithm is as follows:
 * <h2>Assumption:</h2>
 *
 *  * $p = 2^{255} - 19$
 *  * $h = h_0 + 2^{25} * h_1 + 2^{(26+25)} * h_2 + \dots + 2^{230} * h_9$
 * where $0 \le |h_i| \lt 2^{27}$ for all $i=0,\dots,9$.
 *  * $h \cong r \mod p$, i.e. $h = r + q * p$ for some suitable $0 \le r \lt
 * p$ and an integer $q$.
 *
 *
 * Then $q = [2^{-255} * (h + 19 * 2^{-25} * h_9 + 1/2)]$ where $[x] =
 * floor(x)$.
 * <h2>Proof:</h2>
 *
 *
 * We begin with some very raw estimation for the bounds of some expressions:
 *
 *
 * $$ \begin{equation} |h| \lt 2^{230} * 2^{30} = 2^{260} \Rightarrow |r + q *
 * p| \lt 2^{260} \Rightarrow |q| \lt 2^{10}. \\ \Rightarrow -1/4 \le a := 19^2
 * * 2^{-255} * q \lt 1/4. \\ |h - 2^{230} * h_9| = |h_0 + \dots + 2^{204} *
 * h_8| \lt 2^{204} * 2^{30} = 2^{234}. \\ \Rightarrow -1/4 \le b := 19 *
 * 2^{-255} * (h - 2^{230} * h_9) \lt 1/4 \end{equation} $$
 *
 *
 * Therefore $0 \lt 1/2 - a - b \lt 1$.
 *
 *
 * Set $x := r + 19 * 2^{-255} * r + 1/2 - a - b$. Then:
 *
 *
 * $$ 0 \le x \lt 255 - 20 + 19 + 1 = 2^{255} \\ \Rightarrow 0 \le 2^{-255} * x
 * \lt 1. $$
 *
 *
 * Since $q$ is an integer we have
 *
 *
 * $$ [q + 2^{-255} * x] = q \quad (1) $$
 *
 *
 * Have a closer look at $x$:
 *
 *
 * $$ \begin{align} x &amp;= h - q * (2^{255} - 19) + 19 * 2^{-255} * (h - q *
 * (2^{255} - 19)) + 1/2 - 19^2 * 2^{-255} * q - 19 * 2^{-255} * (h - 2^{230} *
 * h_9) \\ &amp;= h - q * 2^{255} + 19 * q + 19 * 2^{-255} * h - 19 * q + 19^2 *
 * 2^{-255} * q + 1/2 - 19^2 * 2^{-255} * q - 19 * 2^{-255} * h + 19 * 2^{-25} *
 * h_9 \\ &amp;= h + 19 * 2^{-25} * h_9 + 1/2 - q^{255}. \end{align} $$
 *
 *
 * Inserting the expression for $x$ into $(1)$ we get the desired expression for
 * $q$.
 *
 * @return the 32-byte encoding of this FieldElement.
 */
internal fun FieldElement.toByteArray(): ByteArray {
    val h = t.copyOf()

    // Step 1:
    // Calculate q
    var q = 19 * h[9] + (1 shl 24) shr 25
    q = h[0] + q shr 26
    q = h[1] + q shr 25
    q = h[2] + q shr 26
    q = h[3] + q shr 25
    q = h[4] + q shr 26
    q = h[5] + q shr 25
    q = h[6] + q shr 26
    q = h[7] + q shr 25
    q = h[8] + q shr 26
    q = h[9] + q shr 25

    // r = h - q * p = h - 2^255 * q + 19 * q
    // First add 19 * q then discard the bit 255
    h[0] += 19 * q

    val carry = IntArray(10)

    carry[0] = h[0] shr 26
    h[1] += carry[0]
    h[0] -= carry[0] shl 26
    carry[1] = h[1] shr 25
    h[2] += carry[1]
    h[1] -= carry[1] shl 25
    carry[2] = h[2] shr 26
    h[3] += carry[2]
    h[2] -= carry[2] shl 26
    carry[3] = h[3] shr 25
    h[4] += carry[3]
    h[3] -= carry[3] shl 25
    carry[4] = h[4] shr 26
    h[5] += carry[4]
    h[4] -= carry[4] shl 26
    carry[5] = h[5] shr 25
    h[6] += carry[5]
    h[5] -= carry[5] shl 25
    carry[6] = h[6] shr 26
    h[7] += carry[6]
    h[6] -= carry[6] shl 26
    carry[7] = h[7] shr 25
    h[8] += carry[7]
    h[7] -= carry[7] shl 25
    carry[8] = h[8] shr 26
    h[9] += carry[8]
    h[8] -= carry[8] shl 26
    carry[9] = h[9] shr 25
    h[9] -= carry[9] shl 25

    // Step 2 (straight forward conversion):
    return byteArrayOf(
        h[0].toByte(),
        (h[0] shr 8).toByte(),
        (h[0] shr 16).toByte(),
        (h[0] shr 24 or (h[1] shl 2)).toByte(),
        (h[1] shr 6).toByte(),
        (h[1] shr 14).toByte(),
        (h[1] shr 22 or (h[2] shl 3)).toByte(),
        (h[2] shr 5).toByte(),
        (h[2] shr 13).toByte(),
        (h[2] shr 21 or (h[3] shl 5)).toByte(),
        (h[3] shr 3).toByte(),
        (h[3] shr 11).toByte(),
        (h[3] shr 19 or (h[4] shl 6)).toByte(),
        (h[4] shr 2).toByte(),
        (h[4] shr 10).toByte(),
        (h[4] shr 18).toByte(),
        h[5].toByte(),
        (h[5] shr 8).toByte(),
        (h[5] shr 16).toByte(),
        (h[5] shr 24 or (h[6] shl 1)).toByte(),
        (h[6] shr 7).toByte(),
        (h[6] shr 15).toByte(),
        (h[6] shr 23 or (h[7] shl 3)).toByte(),
        (h[7] shr 5).toByte(),
        (h[7] shr 13).toByte(),
        (h[7] shr 21 or (h[8] shl 4)).toByte(),
        (h[8] shr 4).toByte(),
        (h[8] shr 12).toByte(),
        (h[8] shr 20 or (h[9] shl 6)).toByte(),
        (h[9] shr 2).toByte(),
        (h[9] shr 10).toByte(),
        (h[9] shr 18).toByte(),
    )
}

/**
 * Load a FieldElement from the low 255 bits of a 256-bit input.
 *
 * @param `in` The 32-byte representation.
 * @return The field element in its $2^{25.5}$ bit representation.
 */
internal fun ByteArray.toFieldElement(): FieldElement {
    val h = longArrayOf(
        load4(this, 0),
        (load3(this, 4) shl 6),
        (load3(this, 7) shl 5),
        (load3(this, 10) shl 3),
        (load3(this, 13) shl 2),
        load4(this, 16),
        (load3(this, 20) shl 7),
        (load3(this, 23) shl 5),
        (load3(this, 26) shl 4),
        (load3(this, 29) and 0x7FFFFF shl 2)
    )

    val carry = LongArray(10)

    // Remember: 2^255 congruent 19 modulo p
    carry[9] = h[9] + (1 shl 24).toLong() shr 25
    h[0] += carry[9] * 19
    h[9] -= carry[9] shl 25
    carry[1] = h[1] + (1 shl 24).toLong() shr 25
    h[2] += carry[1]
    h[1] -= carry[1] shl 25
    carry[3] = h[3] + (1 shl 24).toLong() shr 25
    h[4] += carry[3]
    h[3] -= carry[3] shl 25
    carry[5] = h[5] + (1 shl 24).toLong() shr 25
    h[6] += carry[5]
    h[5] -= carry[5] shl 25
    carry[7] = h[7] + (1 shl 24).toLong() shr 25
    h[8] += carry[7]
    h[7] -= carry[7] shl 25
    carry[0] = h[0] + (1 shl 25).toLong() shr 26
    h[1] += carry[0]
    h[0] -= carry[0] shl 26
    carry[2] = h[2] + (1 shl 25).toLong() shr 26
    h[3] += carry[2]
    h[2] -= carry[2] shl 26
    carry[4] = h[4] + (1 shl 25).toLong() shr 26
    h[5] += carry[4]
    h[4] -= carry[4] shl 26
    carry[6] = h[6] + (1 shl 25).toLong() shr 26
    h[7] += carry[6]
    h[6] -= carry[6] shl 26
    carry[8] = h[8] + (1 shl 25).toLong() shr 26
    h[9] += carry[8]
    h[8] -= carry[8] shl 26

    return FieldElement(h.map { it.toInt() }.toIntArray())
}