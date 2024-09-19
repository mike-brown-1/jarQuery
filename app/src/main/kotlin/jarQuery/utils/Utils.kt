package jarQuery.utils

import jarQuery.debug
import java.io.File
import java.io.FilenameFilter

fun error(message: String, result: Int): Int {
    println("**** ERROR: $message")
    return result
}

fun debugMsg(message: String) {
    if (debug) {
        println("DEBUG: $message")
    }
}

fun getJarFiles(directory: File): List<File> {
    val jarFilter = FilenameFilter { _, name ->
        name.endsWith(".jar")
    }
    val jars = mutableListOf<File>()
    val jarFiles = directory.listFiles(jarFilter)
    if (jarFiles != null) {
        jars.addAll(jarFiles)
    }
    return jars
}

fun listDirectoriesRecursively(startDir: File): List<File> {
    require(startDir.isDirectory) { "The starting path must be a directory" }

    return startDir.walkTopDown()
        .filter { it.isDirectory }
        .toList()
}
