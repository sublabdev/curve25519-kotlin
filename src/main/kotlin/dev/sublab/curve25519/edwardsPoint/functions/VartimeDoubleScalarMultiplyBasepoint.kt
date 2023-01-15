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

package dev.sublab.curve25519.edwardsPoint.functions

import dev.sublab.curve25519.affineNielsPoint.AffineNielsPoint
import dev.sublab.curve25519.edwardsPoint.EdwardsPoint
import dev.sublab.curve25519.projectiveNielsPoint.ProjectiveNielsPoint
import dev.sublab.curve25519.projectiveNielsPoint.functions.buildNafLookupTable
import dev.sublab.curve25519.scalar.Scalar
import dev.sublab.curve25519.scalar.functions.nonAdjacentForm

/**
 * Compute $r = [a]A + [b]B$ in variable time, where $B$ is the Ed25519
 * basepoint.
 *
 * @param a a Scalar.
 * @param ep an EdwardsPoint.
 * @param b a Scalar.
 * @return $[a]A + [b]B$
 */
fun vartimeDoubleScalarMultiplyBasepoint(
    a: Scalar,
    ep: EdwardsPoint,
    b: Scalar
): EdwardsPoint {
    val aNaf = a.nonAdjacentForm()
    val bNaf = b.nonAdjacentForm()
    val tableA = ProjectiveNielsPoint.buildNafLookupTable(ep)
    val tableB = AffineNielsPoint.AFFINE_ODD_MULTIPLES_OF_BASEPOINT

    var i = 255
    while (i >= 0) {
        if (aNaf[i].toInt() != 0 || bNaf[i].toInt() != 0) break
        --i
    }

    var r = EdwardsPoint.IDENTITY.toProjective()
    while (i >= 0) {
        var t = r.double()

        if (aNaf[i] > 0) {
            t = t.toExtended().add(tableA.select(aNaf[i]))
        } else if (aNaf[i] < 0) {
            t = t.toExtended().subtract(tableA.select(-aNaf[i]))
        }

        if (bNaf[i] > 0) {
            t = t.toExtended().add(tableB.select(bNaf[i]))
        } else if (bNaf[i] < 0) {
            t = t.toExtended().subtract(tableB.select(-bNaf[i]))
        }

        r = t.toProjective()
        --i
    }

    return r.toExtended()
}