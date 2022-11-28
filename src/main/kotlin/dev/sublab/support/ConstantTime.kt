package dev.sublab.support

/**
 * Constant-time functions.
 */
object ConstantTime {
    /**
     * Constant-time byte comparison.
     *
     * @param b a byte, represented as an int
     * @param c a byte, represented as an int
     * @return true if b and c are equal, false otherwise.
     */
    fun equal(b: Int, c: Int): Boolean {
        var result = 0
        val xor = b xor c
        for (i in 0..7) {
            result = result or (xor shr i)
        }

        return (result xor 0x01 and 0x01) == 1
    }

    fun equal(b: Boolean, c: Int) = equal(if (b) 1 else 0, c)

    /**
     * Constant-time ByteArray comparison.
     *
     * Fails fast if the lengths differ.
     *
     * @param b a ByteArray
     * @param c a ByteArray
     * @return true if b and c are equal, false otherwise.
     */
    fun equal(b: ByteArray, c: ByteArray): Boolean {
        // Fail-fast if the lengths differ
        if (b.size != c.size) {
            return false
        }

        // Now use a constant-time comparison
        var result = 0
        for (i in b.indices) {
            result = result or (b[i].toInt() xor c[i].toInt())
        }

        return equal(result, 0)
    }

    /**
     * Constant-time determine if byte is negative.
     *
     * @param b the byte to check, represented as an int.
     * @return 1 if the byte is negative, 0 otherwise.
     */
    fun isNegative(b: Int) = (b shr 8 and 1) == 1

    /**
     * Get the i'th bit of a byte array.
     *
     * @param h the ByteArray.
     * @param i the bit index.
     * @return 0 or 1, the value of the i'th bit in h
     */
    fun bit(h: ByteArray, i: Int) = h[i shr 3].toInt() shr (i and 7) and 1
}