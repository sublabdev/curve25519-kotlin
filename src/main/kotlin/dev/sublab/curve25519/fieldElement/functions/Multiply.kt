package dev.sublab.curve25519.fieldElement.functions

import dev.sublab.curve25519.fieldElement.FieldElement

/**
 * $h = f * g$
 *
 *
 * Can overlap $h$ with $f$ or $g$.
 *
 *
 * Preconditions:
 *
 *  * $|f|$ bounded by $1.65*2^{26},1.65*2^{25},1.65*2^{26},1.65*2^{25},$ etc.
 *  * $|g|$ bounded by $1.65*2^{26},1.65*2^{25},1.65*2^{26},1.65*2^{25},$ etc.
 *
 *
 * Postconditions:
 *
 *  * $|h|$ bounded by $1.01*2^{25},1.01*2^{24},1.01*2^{25},1.01*2^{24},$ etc.
 *
 *
 * Notes on implementation strategy:
 *
 *
 * Using schoolbook multiplication. Karatsuba would save a little in some cost
 * models.
 *
 *
 * Most multiplications by 2 and 19 are 32-bit precomputations; cheaper than
 * 64-bit postcomputations.
 *
 *
 * There is one remaining multiplication by 19 in the carry chain; one *19
 * precomputation can be merged into this, but the resulting data flow is
 * considerably less clean.
 *
 *
 * There are 12 carries below. 10 of them are 2-way parallelizable and
 * vectorizable. Can get away with 11 carries, but then data flow is much
 * deeper.
 *
 *
 * With tighter constraints on inputs can squeeze carries into int32.
 *
 * @param value The field element to multiply.
 * @return The (reasonably reduced) field element this * val.
 */
internal fun FieldElement.multiply(value: FieldElement): FieldElement {
    val g = value.t

    val g1m19 = 19 * g[1] /* 1.959375*2^29 */
    val g2m19 = 19 * g[2] /* 1.959375*2^30; still ok */
    val g3m19 = 19 * g[3]
    val g4m19 = 19 * g[4]
    val g5m19 = 19 * g[5]
    val g6m19 = 19 * g[6]
    val g7m19 = 19 * g[7]
    val g8m19 = 19 * g[8]
    val g9m19 = 19 * g[9]
    val f1m2 = 2 * t[1]
    val f3m2 = 2 * t[3]
    val f5m2 = 2 * t[5]
    val f7m2 = 2 * t[7]
    val f9m2 = 2 * t[9]

    val f0g0 = t[0] * g[0].toLong()
    val f0g1 = t[0] * g[1].toLong()
    val f0g2 = t[0] * g[2].toLong()
    val f0g3 = t[0] * g[3].toLong()
    val f0g4 = t[0] * g[4].toLong()
    val f0g5 = t[0] * g[5].toLong()
    val f0g6 = t[0] * g[6].toLong()
    val f0g7 = t[0] * g[7].toLong()
    val f0g8 = t[0] * g[8].toLong()
    val f0g9 = t[0] * g[9].toLong()
    val f1g0 = t[1] * g[0].toLong()
    val f1g1m2 = f1m2 * g[1].toLong()
    val f1g2 = t[1] * g[2].toLong()
    val f1g3m2 = f1m2 * g[3].toLong()
    val f1g4 = t[1] * g[4].toLong()
    val f1g5m2 = f1m2 * g[5].toLong()
    val f1g6 = t[1] * g[6].toLong()
    val f1g7m2 = f1m2 * g[7].toLong()
    val f1g8 = t[1] * g[8].toLong()
    val f1g9m38 = f1m2 * g9m19.toLong()
    val f2g0 = t[2] * g[0].toLong()
    val f2g1 = t[2] * g[1].toLong()
    val f2g2 = t[2] * g[2].toLong()
    val f2g3 = t[2] * g[3].toLong()
    val f2g4 = t[2] * g[4].toLong()
    val f2g5 = t[2] * g[5].toLong()
    val f2g6 = t[2] * g[6].toLong()
    val f2g7 = t[2] * g[7].toLong()
    val f2g8m19 = t[2] * g8m19.toLong()
    val f2g9m19 = t[2] * g9m19.toLong()
    val f3g0 = t[3] * g[0].toLong()
    val f3g1m2 = f3m2 * g[1].toLong()
    val f3g2 = t[3] * g[2].toLong()
    val f3g3m2 = f3m2 * g[3].toLong()
    val f3g4 = t[3] * g[4].toLong()
    val f3g5m2 = f3m2 * g[5].toLong()
    val f3g6 = t[3] * g[6].toLong()
    val f3g7m38 = f3m2 * g7m19.toLong()
    val f3g8m19 = t[3] * g8m19.toLong()
    val f3g9m38 = f3m2 * g9m19.toLong()
    val f4g0 = t[4] * g[0].toLong()
    val f4g1 = t[4] * g[1].toLong()
    val f4g2 = t[4] * g[2].toLong()
    val f4g3 = t[4] * g[3].toLong()
    val f4g4 = t[4] * g[4].toLong()
    val f4g5 = t[4] * g[5].toLong()
    val f4g6m19 = t[4] * g6m19.toLong()
    val f4g7m19 = t[4] * g7m19.toLong()
    val f4g8m19 = t[4] * g8m19.toLong()
    val f4g9m19 = t[4] * g9m19.toLong()
    val f5g0 = t[5] * g[0].toLong()
    val f5g1m2 = f5m2 * g[1].toLong()
    val f5g2 = t[5] * g[2].toLong()
    val f5g3m2 = f5m2 * g[3].toLong()
    val f5g4 = t[5] * g[4].toLong()
    val f5g5m38 = f5m2 * g5m19.toLong()
    val f5g6m19 = t[5] * g6m19.toLong()
    val f5g7m38 = f5m2 * g7m19.toLong()
    val f5g8m19 = t[5] * g8m19.toLong()
    val f5g9m38 = f5m2 * g9m19.toLong()
    val f6g0 = t[6] * g[0].toLong()
    val f6g1 = t[6] * g[1].toLong()
    val f6g2 = t[6] * g[2].toLong()
    val f6g3 = t[6] * g[3].toLong()
    val f6g4m19 = t[6] * g4m19.toLong()
    val f6g5m19 = t[6] * g5m19.toLong()
    val f6g6m19 = t[6] * g6m19.toLong()
    val f6g7m19 = t[6] * g7m19.toLong()
    val f6g8m19 = t[6] * g8m19.toLong()
    val f6g9m19 = t[6] * g9m19.toLong()
    val f7g0 = t[7] * g[0].toLong()
    val f7g1m2 = f7m2 * g[1].toLong()
    val f7g2 = t[7] * g[2].toLong()
    val f7g3m38 = f7m2 * g3m19.toLong()
    val f7g4m19 = t[7] * g4m19.toLong()
    val f7g5m38 = f7m2 * g5m19.toLong()
    val f7g6m19 = t[7] * g6m19.toLong()
    val f7g7m38 = f7m2 * g7m19.toLong()
    val f7g8m19 = t[7] * g8m19.toLong()
    val f7g9m38 = f7m2 * g9m19.toLong()
    val f8g0 = t[8] * g[0].toLong()
    val f8g1 = t[8] * g[1].toLong()
    val f8g2m19 = t[8] * g2m19.toLong()
    val f8g3m19 = t[8] * g3m19.toLong()
    val f8g4m19 = t[8] * g4m19.toLong()
    val f8g5m19 = t[8] * g5m19.toLong()
    val f8g6m19 = t[8] * g6m19.toLong()
    val f8g7m19 = t[8] * g7m19.toLong()
    val f8g8m19 = t[8] * g8m19.toLong()
    val f8g9m19 = t[8] * g9m19.toLong()
    val f9g0 = t[9] * g[0].toLong()
    val f9g1m38 = f9m2 * g1m19.toLong()
    val f9g2m19 = t[9] * g2m19.toLong()
    val f9g3m38 = f9m2 * g3m19.toLong()
    val f9g4m19 = t[9] * g4m19.toLong()
    val f9g5m38 = f9m2 * g5m19.toLong()
    val f9g6m19 = t[9] * g6m19.toLong()
    val f9g7m38 = f9m2 * g7m19.toLong()
    val f9g8m19 = t[9] * g8m19.toLong()
    val f9g9m38 = f9m2 * g9m19.toLong()

    /**
     * Remember: 2^255 congruent 19 modulo p. h = h0 * 2^0 + h1 * 2^26 + h2 *
     * 2^(26+25) + h3 * 2^(26+25+26) + ... + h9 * 2^(5*26+5*25). So to get the real
     * number we would have to multiply the coefficients with the corresponding
     * powers of 2. To get an idea what is going on below, look at the calculation
     * of h0: h0 is the coefficient to the power 2^0 so it collects (sums) all
     * products that have the power 2^0. f0 * g0 really is f0 * 2^0 * g0 * 2^0 = (f0
     * * g0) * 2^0. f1 * g9 really is f1 * 2^26 * g9 * 2^230 = f1 * g9 * 2^256 = 2 *
     * f1 * g9 * 2^255 congruent 2 * 19 * f1 * g9 * 2^0 modulo p. f2 * g8 really is
     * f2 * 2^51 * g8 * 2^204 = f2 * g8 * 2^255 congruent 19 * f2 * g8 * 2^0 modulo
     * p. and so on...
     */

    val h = longArrayOf(
        f0g0 + f1g9m38 + f2g8m19 + f3g7m38 + f4g6m19 + f5g5m38 + f6g4m19 + f7g3m38 + f8g2m19 + f9g1m38,
        f0g1 + f1g0 + f2g9m19 + f3g8m19 + f4g7m19 + f5g6m19 + f6g5m19 + f7g4m19 + f8g3m19 + f9g2m19,
        f0g2 + f1g1m2 + f2g0 + f3g9m38 + f4g8m19 + f5g7m38 + f6g6m19 + f7g5m38 + f8g4m19 + f9g3m38,
        f0g3 + f1g2 + f2g1 + f3g0 + f4g9m19 + f5g8m19 + f6g7m19 + f7g6m19 + f8g5m19 + f9g4m19,
        f0g4 + f1g3m2 + f2g2 + f3g1m2 + f4g0 + f5g9m38 + f6g8m19 + f7g7m38 + f8g6m19 + f9g5m38,
        f0g5 + f1g4 + f2g3 + f3g2 + f4g1 + f5g0 + f6g9m19 + f7g8m19 + f8g7m19 + f9g6m19,
        f0g6 + f1g5m2 + f2g4 + f3g3m2 + f4g2 + f5g1m2 + f6g0 + f7g9m38 + f8g8m19 + f9g7m38,
        f0g7 + f1g6 + f2g5 + f3g4 + f4g3 + f5g2 + f6g1 + f7g0 + f8g9m19 + f9g8m19,
        f0g8 + f1g7m2 + f2g6 + f3g5m2 + f4g4 + f5g3m2 + f6g2 + f7g1m2 + f8g0 + f9g9m38,
        f0g9 + f1g8 + f2g7 + f3g6 + f4g5 + f5g4 + f6g3 + f7g2 + f8g1 + f9g0,
    )

    /*
     * |h0| <= (1.65*1.65*2^52*(1+19+19+19+19)+1.65*1.65*2^50*(38+38+38+38+38)) i.e.
     * |h0| <= 1.4*2^60; narrower ranges for h2, h4, h6, h8 |h1| <=
     * (1.65*1.65*2^51*(1+1+19+19+19+19+19+19+19+19)) i.e. |h1| <= 1.7*2^59;
     * narrower ranges for h3, h5, h7, h9
     */

    val carry = LongArray(10)

    carry[0] = h[0] + (1 shl 25).toLong() shr 26
    h[1] += carry[0]
    h[0] -= carry[0] shl 26
    carry[4] = h[4] + (1 shl 25).toLong() shr 26
    h[5] += carry[4]
    h[4] -= carry[4] shl 26
    /* |h[0]| <= 2^25 */
    /* |h[4]| <= 2^25 */
    /* |h[1]| <= 1.71*2^59 */
    /* |h[5]| <= 1.71*2^59 */
    carry[1] = h[1] + (1 shl 24).toLong() shr 25
    h[2] += carry[1]
    h[1] -= carry[1] shl 25
    carry[5] = h[5] + (1 shl 24).toLong() shr 25
    h[6] += carry[5]
    h[5] -= carry[5] shl 25
    /* |h[1]| <= 2^24; from now on fits into int32 */
    /* |h[5]| <= 2^24; from now on fits into int32 */
    /* |h[2]| <= 1.41*2^60 */
    /* |h[6]| <= 1.41*2^60 */
    carry[2] = h[2] + (1 shl 25).toLong() shr 26
    h[3] += carry[2]
    h[2] -= carry[2] shl 26
    carry[6] = h[6] + (1 shl 25).toLong() shr 26
    h[7] += carry[6]
    h[6] -= carry[6] shl 26
    /* |h[2]| <= 2^25; from now on fits into int32 unchanged */
    /* |h[6]| <= 2^25; from now on fits into int32 unchanged */
    /* |h[3]| <= 1.71*2^59 */
    /* |h[7]| <= 1.71*2^59 */
    carry[3] = h[3] + (1 shl 24).toLong() shr 25
    h[4] += carry[3]
    h[3] -= carry[3] shl 25
    carry[7] = h[7] + (1 shl 24).toLong() shr 25
    h[8] += carry[7]
    h[7] -= carry[7] shl 25
    /* |h[3]| <= 2^24; from now on fits into int32 unchanged */
    /* |h[7]| <= 2^24; from now on fits into int32 unchanged */
    /* |h[4]| <= 1.72*2^34 */
    /* |h[8]| <= 1.41*2^60 */
    carry[4] = h[4] + (1 shl 25).toLong() shr 26
    h[5] += carry[4]
    h[4] -= carry[4] shl 26
    carry[8] = h[8] + (1 shl 25).toLong() shr 26
    h[9] += carry[8]
    h[8] -= carry[8] shl 26
    /* |h[4]| <= 2^25; from now on fits into int32 unchanged */
    /* |h[8]| <= 2^25; from now on fits into int32 unchanged */
    /* |h[5]| <= 1.01*2^24 */
    /* |h[9]| <= 1.71*2^59 */
    carry[9] = h[9] + (1 shl 24).toLong() shr 25
    h[0] += carry[9] * 19
    h[9] -= carry[9] shl 25
    /* |h[9]| <= 2^24; from now on fits into int32 unchanged */
    /* |h[0]| <= 1.1*2^39 */
    carry[0] = h[0] + (1 shl 25).toLong() shr 26
    h[1] += carry[0]
    h[0] -= carry[0] shl 26
    /* |h[0]| <= 2^25; from now on fits into int32 unchanged */
    /* |h[1]| <= 1.01*2^24 */

    return FieldElement(h.map { it.toInt() }.toIntArray())
}