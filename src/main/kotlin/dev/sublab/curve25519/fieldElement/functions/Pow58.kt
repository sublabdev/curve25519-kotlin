package dev.sublab.curve25519.fieldElement.functions

import dev.sublab.curve25519.fieldElement.FieldElement

/**
 * Raises this field element to the power $(p-5)/8 = 2^{252} - 3$.
 *
 *
 * Helper for [.sqrtRatioM1].
 *
 * @return $\text{this}^{(p-5)/8}$.
 */
internal fun FieldElement.powP58(): FieldElement {
    // 2 == 2 * 1
    var t0 = square()

    // 4 == 2 * 2
    var t1 = t0.square()

    // 8 == 2 * 4
    t1 = t1.square()

    // z9 = z1*z8
    t1 = multiply(t1)

    // 11 == 9 + 2
    t0 = t0.multiply(t1)

    // 22 == 2 * 11
    t0 = t0.square()

    // 31 == 22 + 9
    t0 = t1.multiply(t0)

    // 2^6 - 2^1
    t1 = t0.square()

    // 2^10 - 2^5
    for (i in 1..4) {
        t1 = t1.square()
    }

    // 2^10 - 2^0
    t0 = t1.multiply(t0)

    // 2^11 - 2^1
    t1 = t0.square()

    // 2^20 - 2^10
    for (i in 1..9) {
        t1 = t1.square()
    }

    // 2^20 - 2^0
    t1 = t1.multiply(t0)

    // 2^21 - 2^1
    var t2 = t1.square()

    // 2^40 - 2^20
    for (i in 1..19) {
        t2 = t2.square()
    }

    // 2^40 - 2^0
    t1 = t2.multiply(t1)

    // 2^41 - 2^1
    t1 = t1.square()

    // 2^50 - 2^10
    for (i in 1..9) {
        t1 = t1.square()
    }

    // 2^50 - 2^0
    t0 = t1.multiply(t0)

    // 2^51 - 2^1
    t1 = t0.square()

    // 2^100 - 2^50
    for (i in 1..49) {
        t1 = t1.square()
    }

    // 2^100 - 2^0
    t1 = t1.multiply(t0)

    // 2^101 - 2^1
    t2 = t1.square()

    // 2^200 - 2^100
    for (i in 1..99) {
        t2 = t2.square()
    }

    // 2^200 - 2^0
    t1 = t2.multiply(t1)

    // 2^201 - 2^1
    t1 = t1.square()

    // 2^250 - 2^50
    for (i in 1..49) {
        t1 = t1.square()
    }

    // 2^250 - 2^0
    t0 = t1.multiply(t0)

    // 2^251 - 2^1
    t0 = t0.square()

    // 2^252 - 2^2
    t0 = t0.square()

    // 2^252 - 3
    return multiply(t0)
}