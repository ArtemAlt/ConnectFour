import kotlin.math.sqrt

fun main() {
    var square: Double = 0.0
    val pi = 3.14
    var fig = readln()
    when (fig) {
        "triangle" -> {
            val a = readln().toDouble()
            val b = readln().toDouble()
            val c = readln().toDouble()
            var p = (a + b + c) / 2
            square = sqrt(p * (p - a) * (p - b) * (p - c))
        }

        "rectangle" -> {
            val a = readln().toDouble()
            val b = readln().toDouble()
            square = a * b
        }

        "circle" -> {
            var r = readln().toDouble()
            square = pi * r * r
        }
    }
    println(square)
}