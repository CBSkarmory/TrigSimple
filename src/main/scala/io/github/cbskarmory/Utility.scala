package io.github.cbskarmory

object Utility {
    @inline
    def divides(a: Int, b: Int): Boolean = {
        ((a % b) + b) % b == 0
    }
}
