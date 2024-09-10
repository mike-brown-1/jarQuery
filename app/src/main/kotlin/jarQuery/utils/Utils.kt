package jarQuery.utils

import jarQuery.debug

fun error(message: String, result: Int): Int {
    println("**** ERROR: $message")
    return result
}

fun debugMsg(message: String) {
    if (debug) {
        println("DEBUG: $message")
    }
}
