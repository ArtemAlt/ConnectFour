import java.time.LocalTime
import java.time.format.DateTimeFormatter


fun main() {
    var d1 = readln().toInt()
    var d2 = readln().toInt()
    var d3 = readln().toInt()
    var d4 = readln().toInt()
    var d5 = readln().toInt()
    var d6 = readln().toInt()
    println((d4*60*60+d5*60+d6) -(d1*60*60+d2*60+d3))
}