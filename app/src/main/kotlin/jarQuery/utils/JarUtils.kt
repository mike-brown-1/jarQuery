package jarQuery.utils

import jarQuery.data.ClassInfo
import jarQuery.data.JarInfo
import jarQuery.debug
import java.io.File
import java.util.jar.JarFile
import java.util.zip.ZipException

fun isValidFile(file: File): Boolean {
    return file.exists() && file.isFile()
}

fun isValidDirectory(dir: File): Boolean {
    return dir.exists() && dir.isDirectory()
}

fun processFile(file: File, jars: MutableList<JarInfo>): Int {
    var result = 0
    debugMsg("processing file: ${file.name}")
    if (!isValidFile(file)) {
        result = error("${file.name} is not a file or does not exist}", 30)
    } else {
        try {
            val jFile = JarFile(file)
            val jarInfo = JarInfo(jFile.name, mutableMapOf<String, String>(), 99, 0, mutableListOf<ClassInfo>())
            jars.add(jarInfo)

            // TODO add manifest if user asked for it, command line option
            println("----manifest:")
            val manifest = jFile.manifest
            for ((key, value) in manifest.mainAttributes) {
                println("${key}: $value")
            }

            var entryCount = 0
            jFile.stream().forEach { entry ->
                if (!entry.isDirectory && entry.name.endsWith(".class")) {
                    entryCount++
                    val ver = getJavaVersionFromStream(jFile.getInputStream(entry))
                    val classInfo = ClassInfo(entry.name, ver, entry.size)
                    jarInfo.classes.add(classInfo)
                    if (jarInfo.minVersion > ver) {
                        jarInfo.minVersion = ver
                    }
                    if (ver > jarInfo.maxVersion) {
                        jarInfo.maxVersion = ver
                    }
                } else {
                    debugMsg("Skipping: ${entry.name}")
                }
            }
        } catch (e: ZipException) {
            result = error("Not a valid jar file: ${e.message}", 60)
        }
    }
    return result
}

fun processDirectory(directory: File, jars: MutableList<JarInfo>): Int {
    println("processing directory: ${directory.name}")
    return 0
}

fun displayJarInfo(jars: List<JarInfo>) {
    jars.forEach { jar ->
        println("Name: ${jar.name}, min: ${jar.minVersion}, max: ${jar.maxVersion}, classes: ${jar.classes.size}")
        jar.classes.forEach { clazz ->
            println("    name: ${clazz.name}, ver: ${clazz.version}, size: ${clazz.size}")
        }
        println("")
    }
}