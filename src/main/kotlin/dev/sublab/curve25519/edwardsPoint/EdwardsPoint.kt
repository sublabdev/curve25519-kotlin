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

package dev.sublab.curve25519.edwardsPoint

import dev.sublab.curve25519.affineNielsPoint.AffineNielsPoint
import dev.sublab.curve25519.completedPoint.CompletedPoint
import dev.sublab.curve25519.fieldElement.FieldElement
import dev.sublab.curve25519.fieldElement.functions.invert
import dev.sublab.curve25519.fieldElement.functions.multiply
import dev.sublab.curve25519.fieldElement.functions.toByteArray
import dev.sublab.curve25519.projectiveNielsPoint.LookupTable
import dev.sublab.curve25519.projectiveNielsPoint.ProjectiveNielsPoint
import dev.sublab.curve25519.projectiveNielsPoint.functions.buildLookupTable
import dev.sublab.curve25519.projectivePoint.ProjectivePoint
import dev.sublab.curve25519.scalar.Scalar
import dev.sublab.curve25519.scalar.functions.toRadix16
import dev.sublab.curve25519.unpackedScalar.UnpackedScalar
import kotlin.experimental.or

/**
 * An EdwardsPoint represents a point on the Edwards form of Curve25519.
 */
@Suppress("unused")
class EdwardsPoint internal constructor(
    internal val x: FieldElement,
    internal val y: FieldElement,
    internal val z: FieldElement,
    internal val t: FieldElement
) {
    /**
     * Compress this point to CompressedEdwardsY format.
     *
     * @return the encoded point.
     */
    fun compress(): CompressedEdwardsY {
        val recip = z.invert()
        val x = x.multiply(recip)
        val y = y.multiply(recip)
        val s = y.toByteArray()
        val xIsNegative = if (x.isNegative) 1 else 0
        s[31] = s[31] or (xIsNegative shl 7).toByte()

        return CompressedEdwardsY(s)
    }

    /**
     * Constant-time equality check.
     *
     *
     * Compares the encodings of the two EdwardsPoints.
     *
     * @return 1 if this and other are equal, 0 otherwise.
     */
    internal fun ctEquals(other: EdwardsPoint) = compress().ctEquals(other.compress())

    /**
     * Constant-time selection between two EdwardsPoints.
     *
     * @param that the other point.
     * @param b    must be 0 or 1, otherwise results are undefined.
     * @return a copy of this if $b == 0$, or a copy of that if $b == 1$.
     */
    fun ctSelect(that: EdwardsPoint, b: Int) = EdwardsPoint(
        x.ctSelect(that.x, b),
        y.ctSelect(that.y, b),
        z.ctSelect(that.z, b),
        t.ctSelect(that.t, b)
    )

    /**
     * Equality check overridden to be constant-time.
     *
     *
     * Fails fast if the objects are of different types.
     *
     * @return true if this and other are equal, false otherwise.
     */
    override fun equals(other: Any?): Boolean {
        if (other !is EdwardsPoint) {
            return false
        }

        return ctEquals(other)
    }

    override fun hashCode() = compress().hashCode()

    /**
     * Convert the representation of this point from extended coordinates to
     * projective coordinates.
     *
     *
     * Free.
     */
    internal fun toProjective() = ProjectivePoint(x, y, z)

    /**
     * Convert to a ProjectiveNielsPoint.
     */
    internal fun toProjectiveNiels() = ProjectiveNielsPoint(
        y.add(x),
        y.subtract(x),
        z,
        t.multiply(FieldElement.EDWARDS_2D)
    )

    /**
     * Dehomogenize to an AffineNielsPoint.
     */
    internal fun toAffineNiels(): AffineNielsPoint {
        val recip = z.invert()
        val x = x.multiply(recip)
        val y = y.multiply(recip)
        val xy2D = x.multiply(y).multiply(FieldElement.EDWARDS_2D)
        return AffineNielsPoint(y.add(x), y.subtract(x), xy2D)
    }

    /**
     * Point addition.
     *
     * @param q the point to add to this one.
     * @return $P + Q$
     */
    fun add(q: EdwardsPoint) = add(q.toProjectiveNiels()).toExtended()

    /**
     * Point addition.
     *
     * @param q the point to add to this one, in projective "Niels coordinates".
     * @return $P + Q$
     */
    internal fun add(q: ProjectiveNielsPoint): CompletedPoint {
        val yPlusX = y.add(x)
        val yMinusX = y.subtract(x)
        val pp = yPlusX.multiply(q.yPlusX)
        val mm = yMinusX.multiply(q.yMinusX)
        val tt2d = t.multiply(q.t2D)
        val zz = z.multiply(q.z)
        val zz2 = zz.add(zz)

        return CompletedPoint(pp.subtract(mm), pp.add(mm), zz2.add(tt2d), zz2.subtract(tt2d))
    }

    /**
     * Point addition.
     *
     * @param q the point to add to this one, in affine "Niels coordinates".
     * @return $P + q$
     */
    internal fun add(q: AffineNielsPoint): CompletedPoint {
        val yPlusX = y.add(x)
        val yMinusX = y.subtract(x)
        val pp = yPlusX.multiply(q.yPlusX)
        val mm = yMinusX.multiply(q.yMinusX)
        val txy2D = t.multiply(q.xy2D)
        val z2 = z.add(z)

        return CompletedPoint(pp.subtract(mm), pp.add(mm), z2.add(txy2D), z2.subtract(txy2D))
    }

    /**
     * Point subtraction.
     *
     * @param q the point to subtract from this one.
     * @return $P - Q$
     */
    fun subtract(q: EdwardsPoint) = subtract(q.toProjectiveNiels()).toExtended()

    /**
     * Point subtraction.
     *
     * @param q the point to subtract from this one, in projective "Niels
     * coordinates".
     * @return $P - Q$
     */
    internal fun subtract(q: ProjectiveNielsPoint): CompletedPoint {
        val yPlusX = y.add(x)
        val yMinusX = y.subtract(x)
        val pm = yPlusX.multiply(q.yMinusX)
        val mp = yMinusX.multiply(q.yPlusX)
        val tt2d = t.multiply(q.t2D)
        val zz = z.multiply(q.z)
        val zz2 = zz.add(zz)

        return CompletedPoint(pm.subtract(mp), pm.add(mp), zz2.subtract(tt2d), zz2.add(tt2d))
    }

    /**
     * Point subtraction.
     *
     * @param q the point to subtract from this one, in affine "Niels coordinates".
     * @return $P - q$
     */
    internal fun subtract(q: AffineNielsPoint): CompletedPoint {
        val yPlusX = y.add(x)
        val yMinusX = y.subtract(x)
        val pm = yPlusX.multiply(q.yMinusX)
        val mp = yMinusX.multiply(q.yPlusX)
        val txy2D = t.multiply(q.xy2D)
        val z2 = z.add(z)

        return CompletedPoint(pm.subtract(mp), pm.add(mp), z2.subtract(txy2D), z2.add(txy2D))
    }

    /**
     * Point negation.
     *
     * @return $-P$
     */
    fun negate() = EdwardsPoint(x.negate(), y, z, t.negate())

    /**
     * Point doubling.
     *
     * @return $[2]P$
     */
    fun double() = toProjective().double().toExtended()

    /**
     * Constant-time variable-base scalar multiplication.
     *
     * @param s the Scalar to multiply by.
     * @return $[s]P$
     */
    fun multiply(s: Scalar): EdwardsPoint {
        // Construct a lookup table of [P,2P,3P,4P,5P,6P,7P,8P]
        val lookupTable = this.lookupTable

        // Compute
        //
        // s = s_0 + s_1*16^1 + ... + s_63*16^63,
        //
        // with -8 ≤ s_i < 8 for 0 ≤ i < 63 and -8 ≤ s_63 ≤ 8.
        val e: ByteArray = s.toRadix16()

        // Compute s*P as
        //
        // @formatter:off
        //    s*P = P*(s_0 +   s_1*16^1 +   s_2*16^2 + ... +   s_63*16^63)
        //    s*P =  P*s_0 + P*s_1*16^1 + P*s_2*16^2 + ... + P*s_63*16^63
        //    s*P = P*s_0 + 16*(P*s_1 + 16*(P*s_2 + 16*( ... + P*s_63)...))
        // @formatter:on
        //
        // We sum right-to-left.
        var q = IDENTITY
        for (i in 63 downTo 0) {
            q = q.multiplyByPow2(4)
            q = q.add(lookupTable.select(e[i])).toExtended()
        }

        return q
    }

    private val lookupTable: LookupTable by lazy {
        ProjectiveNielsPoint.buildLookupTable(this)
    }

    /**
     * Multiply by the cofactor.
     *
     * @return $[8]P$
     */
    private fun multiplyByCofactor() = multiplyByPow2(3)

    /**
     * Compute $[2^k]P$ by successive doublings.
     *
     * @param k the exponent of 2. Must be positive and non-zero.
     * @return $[2^k]P$
     */
    fun multiplyByPow2(k: Int): EdwardsPoint {
        require(k > 0) { "Exponent must be positive and non-zero" }

        var s = toProjective()
        for (i in 0 until k - 1) {
            s = s.double().toProjective()
        }

        // Unroll last doubling, so we can go directly to extended coordinates.
        return s.double().toExtended()
    }

    /**
     * Determine if this point is the identity.
     *
     * @return true if this point is the identity, false otherwise.
     */
    val isIdentity: Boolean
        get() = ctEquals(IDENTITY)

    /**
     * Determine if this point is in the 8-torsion subgroup $(\mathcal E[8])$, and
     * therefore of small order.
     *
     * @return true if this point is of small order, false otherwise.
     */
    val isSmallOrder: Boolean
        get() = multipliedByCofactor.isIdentity

    private val multipliedByCofactor: EdwardsPoint by lazy {
        multiplyByCofactor()
    }

    /**
     * Determine if this point is contained in the prime-order subgroup $(\mathcal
     * E[\ell])$, and has no torsion component.
     *
     * @return true if this point has zero torsion component and is in the
     * prime-order subgroup, false otherwise.
     */
    val isTorsionFree: Boolean
        get() = multipliedByBasePointOrder.isIdentity

    private val multipliedByBasePointOrder: EdwardsPoint by lazy {
        multiply(UnpackedScalar.BASEPOINT_ORDER)
    }

    override fun toString()
        = """
            EdwardsPoint(
                x: $x,
                y: $y,
                z: $z,
                t: $t
            )
        """.trimIndent()

    companion object {
        val IDENTITY = EdwardsPoint(
            FieldElement.ZERO, FieldElement.ONE,
            FieldElement.ONE, FieldElement.ZERO
        )

        /**
         * The Ed25519 basepoint, as an EdwardsPoint.
         */
        val ED25519_BASEPOINT = EdwardsPoint(
            FieldElement(
                intArrayOf(
                    -14297830, -7645148, 16144683, -16471763, 27570974,
                    -2696100, -26142465, 8378389, 20764389, 8758491
                )
            ),
            FieldElement(
                intArrayOf(
                    -26843541, -6710886, 13421773, -13421773, 26843546,
                    6710886, -13421773, 13421773, -26843546, -6710886
                )
            ),
            FieldElement(intArrayOf(1, 0, 0, 0, 0, 0, 0, 0, 0, 0)),
            FieldElement(
                intArrayOf(
                    28827062, -6116119, -27349572, 244363, 8635006,
                    11264893, 19351346, 13413597, 16611511, -6414980
                )
            )
        )

        /**
         * Table containing pre-computed multiples of the Ed25519 basepoint.
         */
        val ED25519_BASEPOINT_TABLE = EdwardsBasepointTable(ED25519_BASEPOINT)
    }
}