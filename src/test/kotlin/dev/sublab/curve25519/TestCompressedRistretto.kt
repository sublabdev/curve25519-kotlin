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

package dev.sublab.curve25519

import dev.sublab.curve25519.edwardsPoint.CompressedEdwardsY
import dev.sublab.curve25519.ristrettoElement.CompressedRistretto
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFalse

internal class TestCompressedRistretto {
    @Test
    fun constructorDoesNotThrowOnCorrectLength() {
        CompressedRistretto(ByteArray(32))
    }

    @Test
    fun constructorThrowsOnTooShort() {
        assertThrows<IllegalArgumentException> {
            CompressedRistretto(ByteArray(31))
        }
    }

    @Test
    fun constructorThrowsOnTooLong() {
        assertThrows<IllegalArgumentException> {
            CompressedRistretto(ByteArray(33))
        }
    }

    @Test
    fun toByteArray() {
        val s = ByteArray(32)
        s[0] = 0x1f
        assertContentEquals(s, CompressedRistretto(s).toByteArray())
    }

    @Test
    fun equalityRequiresSameClass() {
        val s = ByteArray(32)
        val r = CompressedRistretto(s)
        val e = CompressedEdwardsY(s)
        assertFalse(r.equals(s))
    }
}