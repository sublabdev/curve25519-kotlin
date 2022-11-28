package dev.sublab.curve25519

import dev.sublab.curve25519.edwardsPoint.EdwardsBasepointTable
import dev.sublab.curve25519.edwardsPoint.EdwardsPoint.Companion.ED25519_BASEPOINT
import dev.sublab.curve25519.support.A_SCALAR
import dev.sublab.curve25519.support.A_TIMES_BASEPOINT
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TestEdwardsBasepointTable {
    @Test
    fun scalarMulVsEd25519py() {
        val bt = EdwardsBasepointTable(ED25519_BASEPOINT)
        val ab = bt.multiply(A_SCALAR)
        assertEquals(A_TIMES_BASEPOINT, ab.compress())
    }
}