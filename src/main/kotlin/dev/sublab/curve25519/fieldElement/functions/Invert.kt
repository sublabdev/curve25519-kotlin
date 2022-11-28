package dev.sublab.curve25519.fieldElement.functions

import dev.sublab.curve25519.fieldElement.FieldElement

/**
 * Invert this field element.
 *
 *
 * The inverse is found via Fermat's little theorem:<br></br>
 * $a^p \cong a \mod p$ and therefore $a^{(p-2)} \cong a^{-1} \mod p$
 *
 * @return The inverse of this field element.
 */
internal fun FieldElement.invert(): FieldElement {
    // 2 == 2 * 1
    var t0 = square()

    // 4 == 2 * 2
    var t1 = t0.square()

    // 8 == 2 * 4
    t1 = t1.square()

    // 9 == 8 + 1
    t1 = multiply(t1)

    // 11 == 9 + 2
    t0 = t0.multiply(t1)

    // 22 == 2 * 11
    var t2 = t0.square()

    // 31 == 22 + 9
    t1 = t1.multiply(t2)

    // 2^6 - 2^1
    t2 = t1.square()

    // 2^10 - 2^5
    for (i in 1..4) {
        t2 = t2.square()
    }

    // 2^10 - 2^0
    t1 = t2.multiply(t1)

    // 2^11 - 2^1
    t2 = t1.square()

    // 2^20 - 2^10
    for (i in 1..9) {
        t2 = t2.square()
    }

    // 2^20 - 2^0
    t2 = t2.multiply(t1)

    // 2^21 - 2^1
    var t3 = t2.square()

    // 2^40 - 2^20
    for (i in 1..19) {
        t3 = t3.square()
    }

    // 2^40 - 2^0
    t2 = t3.multiply(t2)

    // 2^41 - 2^1
    t2 = t2.square()

    // 2^50 - 2^10
    for (i in 1..9) {
        t2 = t2.square()
    }

    // 2^50 - 2^0
    t1 = t2.multiply(t1)

    // 2^51 - 2^1
    t2 = t1.square()

    // 2^100 - 2^50
    for (i in 1..49) {
        t2 = t2.square()
    }

    // 2^100 - 2^0
    t2 = t2.multiply(t1)

    // 2^101 - 2^1
    t3 = t2.square()

    // 2^200 - 2^100
    for (i in 1..99) {
        t3 = t3.square()
    }

    // 2^200 - 2^0
    t2 = t3.multiply(t2)

    // 2^201 - 2^1
    t2 = t2.square()

    // 2^250 - 2^50
    for (i in 1..49) {
        t2 = t2.square()
    }

    // 2^250 - 2^0
    t1 = t2.multiply(t1)

    // 2^251 - 2^1
    t1 = t1.square()

    // 2^255 - 2^5
    for (i in 1..4) {
        t1 = t1.square()
    }

    // 2^255 - 21
    return t1.multiply(t0)
}