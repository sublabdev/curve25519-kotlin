package dev.sublab.curve25519

interface CommonLookupTable<T> {
    fun select(x: Int): T
    fun select(x: Byte) = select(x.toInt())
}