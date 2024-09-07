package jarQuery

import java.io.File
import java.util.jar.JarFile

fun isValidFile(file: File): Boolean {
    return file.exists() && file.isFile()
}

fun isValidDirectory(dir: File): Boolean {
    return dir.exists() && dir.isDirectory()
}

fun processFile(file: File): Int {
    println("processing file: ${file.name}")
    return 0
}

fun processDirectory(file: File): Int {
    println("processing directory: ${file.name}")
    return 0
}
