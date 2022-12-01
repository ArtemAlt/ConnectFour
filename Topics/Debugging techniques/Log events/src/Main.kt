fun String?.capitalize(): String? {
    println("Before: $this")
   var s = when {
        isNullOrBlank() -> this
        length == 1 -> uppercase()
        else -> this[0].uppercase() + substring(1)
    }
    println("After: $s")
    return this
}