package dev.sublab.curve25519.fieldElement.functions

import dev.sublab.curve25519.fieldElement.FieldElement

/**
 * $h = 2 * f * f$
 *
 *
 * Can overlap $h$ with $f$.
 *
 *
 * Preconditions:
 *
 *  * $|f|$ bounded by $1.65*2^{26},1.65*2^{25},1.65*2^{26},1.65*2^{25},$ etc.
 *
 *
 * Postconditions:
 *
 *  * $|h|$ bounded by $1.01*2^{25},1.01*2^{24},1.01*2^{25},1.01*2^{24},$ etc.
 *
 *
 * See [.multiply] for discussion of implementation
 * strategy.
 *
 * @return The (reasonably reduced) square of this field element times 2.
 */
internal fun FieldElement.squareAndDouble() = square(2)