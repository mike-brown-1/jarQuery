package jarQuery

fun error(message: String, result: Int): Int {
    println("**** ERROR: $message")
    return result
}