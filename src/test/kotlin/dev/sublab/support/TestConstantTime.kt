package dev.sublab.support

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TestConstantTime {
    @Test
    fun equalOnByte() {
        assertEquals(ConstantTime.equal(0, 0), true)
        assertEquals(ConstantTime.equal(1, 1), true)
        assertEquals(ConstantTime.equal(1, 0), false)
        assertEquals(ConstantTime.equal(1, 127), false)
        assertEquals(ConstantTime.equal(-127, 127), false)
        assertEquals(ConstantTime.equal(-42, -42), true)
        assertEquals(ConstantTime.equal(255, 255), true)
        assertEquals(ConstantTime.equal(-255, -256), false)
    }

    @Test
    fun equalOnByteArraysWithSingleDifference() {
        val zero = ByteArray(32)
        val one = ByteArray(32)
        one[0] = 1
        assertEquals(ConstantTime.equal(zero, zero), true)
        assertEquals(ConstantTime.equal(one, one), true)
        assertEquals(ConstantTime.equal(one, zero), false)
        assertEquals(ConstantTime.equal(zero, one), false)
    }

    @Test
    fun equalOnByteArraysWithDifferentLengths() {
        val zeroNine = ByteArray(9)
        val zeroTen = ByteArray(10)
        assertEquals(ConstantTime.equal(zeroNine, zeroNine), true)
        assertEquals(ConstantTime.equal(zeroTen, zeroTen), true)
        assertEquals(ConstantTime.equal(zeroNine, zeroTen), false)
        assertEquals(ConstantTime.equal(zeroTen, zeroNine), false)
    }

    @Test
    fun equalOnByteArraysWithRandomData() {
        val random = Random(758094325)
        for (i in 1..32) {
            val a = ByteArray(i)
            val b = ByteArray(i)
            random.nextBytes(a)
            random.nextBytes(b)
            assertEquals(ConstantTime.equal(a, a), true)
            assertEquals(ConstantTime.equal(b, b), true)
            assertEquals(ConstantTime.equal(a, b), false)
            assertEquals(ConstantTime.equal(b, a), false)

            // Test mutation in MSB
            val aPrime = a.copyOf(i)
            assertEquals(ConstantTime.equal(a, aPrime), true)
            aPrime[i-1] = (aPrime[i-1].toInt() + 1).toByte()
            assertEquals(ConstantTime.equal(a, aPrime), false)
        }
    }

    @Test
    fun isNegative() {
        assertEquals(ConstantTime.isNegative(0), false)
        assertEquals(ConstantTime.isNegative(1), false)
        assertEquals(ConstantTime.isNegative(-1), true)
        assertEquals(ConstantTime.isNegative(32), false)
        assertEquals(ConstantTime.isNegative(-100), true)
        assertEquals(ConstantTime.isNegative(127), false)
        assertEquals(ConstantTime.isNegative(-255), true)
    }

    @Test
    fun bit() {
        assertEquals(ConstantTime.bit(byteArrayOf(0), 0), 0)
        assertEquals(ConstantTime.bit(byteArrayOf(8), 3), 1)
        assertEquals(ConstantTime.bit(byteArrayOf(1, 2, 3), 9), 1)
        assertEquals(ConstantTime.bit(byteArrayOf(1, 2, 3), 15), 0)
        assertEquals(ConstantTime.bit(byteArrayOf(1, 2, 3), 16), 1)
    }
}