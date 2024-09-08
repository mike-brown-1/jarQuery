package jarQuery

import java.io.File
import java.util.jar.JarFile
import java.util.zip.ZipException

fun isValidFile(file: File): Boolean {
    return file.exists() && file.isFile()
}

fun isValidDirectory(dir: File): Boolean {
    return dir.exists() && dir.isDirectory()
}

fun processFile(file: File): Int {
    var result = 0
    if (debug) {
        println("processing file: ${file.name}")
    }
    if (!isValidFile(file)) {
        result = error("${file.name} is not a file or does not exist}", 30)
    } else {
        try {
            val jFile = JarFile(file)
            println("----manifest:")
            val manifest = jFile.manifest
            for ((key, value) in manifest.mainAttributes) {
                println("${key}: $value")
            }

            println("---- jar contents:")
            jFile.stream().forEach { entry ->
                if (!entry.isDirectory && entry.name.endsWith(".class")) {
                    val ver = getJavaVersionFromStream(jFile.getInputStream(entry))
                    println("class: ${entry.name}, ver: ${ver}")
                } else {
                    println("skipping: ${entry.name}")
                }
            }
        } catch (e: ZipException) {
            result = error("Not a valid jar file: ${e.message}", 60)
        }
    }
    return result
}

fun processDirectory(directory: File): Int {
    println("processing directory: ${directory.name}")
    return 0
}
