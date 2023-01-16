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

package dev.sublab.curve25519.scalar.functions

import dev.sublab.curve25519.scalar.Scalar
import dev.sublab.curve25519.unpackedScalar.UnpackedScalar.Companion.R
import dev.sublab.curve25519.unpackedScalar.functions.montgomeryReduce
import dev.sublab.curve25519.unpackedScalar.functions.toByteArray
import dev.sublab.curve25519.unpackedScalar.functions.toUnpackedScalar

/**
 * Reduce the given scalar modulo $\ell$.
 *
 * @return the reduced Scalar.
 */
internal fun ByteArray.reduceToScalar()
    = Scalar(montgomeryReduce(toUnpackedScalar().mulInternal(R)).toByteArray())
