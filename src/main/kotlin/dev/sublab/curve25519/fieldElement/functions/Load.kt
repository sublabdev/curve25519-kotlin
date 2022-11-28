package dev.sublab.curve25519.fieldElement.functions

@Suppress("NAME_SHADOWING")
internal fun load3(byteArray: ByteArray, offset: Int): Long {
    var offset = offset
    var result = byteArray[offset++].toInt() and 0xff
    result = result or (byteArray[offset++].toInt() and 0xff shl 8)
    result = result or (byteArray[offset].toInt() and 0xff shl 16)

    return result.toLong()
}

@Suppress("NAME_SHADOWING")
internal fun load4(byteArray: ByteArray, offset: Int): Long {
    var offset = offset
    var result = byteArray[offset++].toInt() and 0xff
    result = result or (byteArray[offset++].toInt() and 0xff shl 8)
    result = result or (byteArray[offset++].toInt() and 0xff shl 16)
    result = result or (byteArray[offset].toInt() shl 24)

    return result.toLong() and 0xffffffffL
}