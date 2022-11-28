package dev.sublab.support

internal inline fun <reified T> Array(size: Int, initWithPrevious: (T?, Int) -> T): Array<T> {
    var previous: T? = null
    return Array(size) { index ->
        val current = initWithPrevious(previous, index)
        previous = current
        current
    }
}