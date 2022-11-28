package dev.sublab.curve25519.unpackedScalar.functions

import dev.sublab.curve25519.unpackedScalar.*

/**
 * Pack the limbs of this UnpackedScalar into 32 bytes.
 */
internal fun UnpackedScalar.toByteArray() = byteArrayOf(
    // All limbs are 29 bits).toByte(), but let's use the unsigned right shift anyway.
    (s[0] ushr 0).toByte(), (s[0] ushr 8).toByte(), (s[0] ushr 16).toByte(), ((s[0] ushr 24) or (s[1] shl 5)).toByte(),
    (s[1] ushr 3).toByte(), (s[1] ushr 11).toByte(), (s[1] ushr 19).toByte(), ((s[1] ushr 27) or (s[2] shl 2)).toByte(),
    (s[2] ushr 6).toByte(), (s[2] ushr 14).toByte(), ((s[2] ushr 22) or (s[3] shl 7)).toByte(),
    (s[3] ushr 1).toByte(), (s[3] ushr 9).toByte(), (s[3] ushr 17).toByte(), ((s[3] ushr 25) or (s[4] shl 4)).toByte(),
    (s[4] ushr 4).toByte(), (s[4] ushr 12).toByte(), (s[4] ushr 20).toByte(), ((s[4] ushr 28) or (s[5] shl 1)).toByte(),
    (s[5] ushr 7).toByte(), (s[5] ushr 15).toByte(), ((s[5] ushr 23) or (s[6] shl 6)).toByte(),
    (s[6] ushr 2).toByte(), (s[6] ushr 10).toByte(), (s[6] ushr 18).toByte(), ((s[6] ushr 26) or (s[7] shl 3)).toByte(),
    (s[7] ushr 5).toByte(), (s[7] ushr 13).toByte(), (s[7] ushr 21).toByte(),
    (s[8] ushr 0).toByte(), (s[8] ushr 8).toByte(), (s[8] ushr 16).toByte(),
)

/**
 * Unpack a 32 byte / 256 bit scalar into 9 29-bit limbs.
 */
internal fun ByteArray.toUnpackedScalar(): UnpackedScalar {
    if (size != 32) {
        throw IllegalArgumentException("Input must by 32 bytes")
    }

    val words = IntArray(8)
    for (i in 0..7) {
        for (j in 0..3) {
            words[i] = words[i] or (((this[(i * 4) + j]).toInt() and 0xff) shl (j * 8))
        }
    }

    return UnpackedScalar(intArrayOf(
        words[0] and MASK_29_BITS,
        ((words[0] ushr 29) or (words[1] shl 3)) and MASK_29_BITS,
        ((words[1] ushr 26) or (words[2] shl 6)) and MASK_29_BITS,
        ((words[2] ushr 23) or (words[3] shl 9)) and MASK_29_BITS,
        ((words[3] ushr 20) or (words[4] shl 12)) and MASK_29_BITS,
        ((words[4] ushr 17) or (words[5] shl 15)) and MASK_29_BITS,
        ((words[5] ushr 14) or (words[6] shl 18)) and MASK_29_BITS,
        ((words[6] ushr 11) or (words[7] shl 21)) and MASK_29_BITS,
        (words[7] ushr 8) and MASK_24_BITS
    ))
}