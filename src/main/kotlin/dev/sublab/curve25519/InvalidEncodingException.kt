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

/**
 * Thrown to indicate that a [dev.sublab.curve25519.edwardsPoint.CompressedEdwardsY] or
 * [dev.sublab.curve25519.ristrettoElement.CompressedRistretto] was an invalid encoding of an
 * [dev.sublab.curve25519.edwardsPoint.EdwardsPoint] or [dev.sublab.curve25519.ristrettoElement.RistrettoElement].
 */
class InvalidEncodingException internal constructor(msg: String) : Exception(msg)