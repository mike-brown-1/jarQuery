package jarQuery.utils

import jarQuery.data.ClassInfo
import jarQuery.data.JarInfo
import jarQuery.manifest
import jarQuery.classes
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.jar.JarFile
import java.util.zip.ZipException
import kotlin.collections.component1
import kotlin.collections.component2

val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)

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

            if (manifest) {
                val manifest = jFile.manifest
                for ((key, value) in manifest.mainAttributes) {
                    jarInfo.manifest["$key"] = value.toString()
                }
            }

            var entryCount = 0
            jFile.stream().forEach { entry ->
                if (!entry.isDirectory && entry.name.endsWith(".class")
                    && !entry.name.startsWith("META-INF")) {
                    val ver = getJavaVersionFromStream(jFile.getInputStream(entry))
                    if (ver == INVALID_CLASS) {
                        println("ERROR: ${entry.name} in ${jFile.name} is not a valid class file")
                    } else if (ver == UNKNOWN_MAJOR_VERION) {
                        println("ERROR: ${entry.name} in ${jFile.name} has unknown major version number")
                    } else {
                        entryCount++
                        if (jarInfo.minVersion > ver) {
                            jarInfo.minVersion = ver
                        }
                        if (ver > jarInfo.maxVersion) {
                            jarInfo.maxVersion = ver
                        }
                        val modified = entry.lastModifiedTime.toInstant()
                        val localDataTime = LocalDateTime.ofInstant(modified, ZoneId.systemDefault())
                        val classInfo = ClassInfo(entry.name, ver, entry.size, localDataTime)
                        jarInfo.classes.add(classInfo)
                    }
                }
            }
        } catch (e: ZipException) {
            result = error("Not a valid jar file: ${e.message}", 60)
        }
    }
    return result
}

fun processDirectory(directory: File, jars: MutableList<JarInfo>): Int {
    var result = 0
    if (isValidDirectory(directory)) {
        debugMsg("processing directory: ${directory.name}")
        val jarFiles = getJarFiles(directory)
        if (jarFiles.isEmpty()) {
            println("No jar files found in ${directory.name}")
        } else {
            jarFiles.forEach { jar ->
                processFile(jar, jars)
            }
        }
    } else  {
        println("${directory.name} is not a valid directory")
        result = 40
    }
    return result
}

fun displayJarInfo(jars: List<JarInfo>) {
    jars.forEach { jar ->
        println("Name: ${jar.name}, min: ${jar.minVersion}, max: ${jar.maxVersion}, classes: ${jar.classes.size}")
        if (manifest) {
            println("    Manifest:")
            for ((key, value) in jar.manifest) {
                println("        $key: $value")
            }
            println()
        }
        if (classes) {
            jar.classes.forEach { clazz ->
                println("    name: ${clazz.name}, ver: ${clazz.version}, size: ${clazz.size}, "
                        + "modified: ${clazz.modified.format(formatter)}")

            }
            println("")
        }
    }
}
