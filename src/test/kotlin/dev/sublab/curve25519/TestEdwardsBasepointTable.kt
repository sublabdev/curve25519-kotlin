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