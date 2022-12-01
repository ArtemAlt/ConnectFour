fun main() {
    var input = readln()
    var splitInput = input.split("")
    var index = 0
    var result = 0;
    for (i in index..splitInput.lastIndex) {
        try {
            result = splitInput.get(i).toInt()
            break
        } catch (e: Exception) {
            index++
        }
    }
    println(result)

}