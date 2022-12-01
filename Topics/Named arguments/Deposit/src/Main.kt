fun main() {
    val s1 = readln()
    val s2 = readln().toInt()
    when (s1) {
        "amount" -> println(calculate(principalAmount = s2))
        "percent" -> println(calculate(rateOfInterest = s2))
        "years" -> println(calculate(time = s2))
    }
}

fun calculate(principalAmount: Int = 1000, rateOfInterest: Int = 5, time: Int = 10): Int {
    return (principalAmount.toDouble() * Math.pow((1 + rateOfInterest.toDouble() / 100.00), time.toDouble())).toInt()
}