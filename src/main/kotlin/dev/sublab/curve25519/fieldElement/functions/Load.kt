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