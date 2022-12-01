fun f(x: Double): Double {
    var result: Double = if (x <= 0) {
        f1(x)
    } else if ( 0.0 <x && x<1.0) {
        f2(x)
    } else if (x >= 1.0) {
        f3(x)
    } else{
        Double.NEGATIVE_INFINITY
    }
    return result
}

// implement your functions here
fun f1(x: Double): Double {
    return x * x + 1
}

fun f2(x: Double): Double {
    return 1 / (x * x)
}

fun f3(x: Double): Double {
    return x * x - 1
}