package jarQuery.utils

import jarQuery.data.ClassInfo
import jarQuery.data.JarInfo
import jarQuery.manifest
import jarQuery.classes
import jarQuery.maxVersion
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

fun processFile(file: File): Result<JarInfo> {
    var result = 0
    debugMsg("processing file: ${file.name}")
    if (!isValidFile(file)) {
        return Result.failure(Exception("${file.name} is not a file or does not exist"))
    } else {
        try {
            val jFile = JarFile(file)
            val jarInfo = JarInfo(jFile.name, mutableMapOf<String, String>(), 99, 0, mutableListOf<ClassInfo>())

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
                    val result = getJavaVersionFromStream(jFile.getInputStream(entry))
                    result.onFailure { ex ->
                        println("**** ERROR: ${ex.message} for class: ${entry.name} in: ${jFile.name}")
                    }
                    result.onSuccess { ver ->
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
            return Result.success(jarInfo)
        } catch (e: ZipException) {
            return Result.failure(Exception("Not a valid jar file: ${e.message}"))
        }
    }
}

fun processDirectory(directory: File, jars: MutableList<JarInfo>): Int {
    var result = 0
    if (isValidDirectory(directory)) {
        debugMsg("processing directory: ${directory.absolutePath}")
        val jarFiles = getJarFiles(directory)
        jarFiles.forEach { jar ->
            val result = processFile(jar)
            result.onSuccess { jarInfo -> jars.add(jarInfo) }
            result.onFailure { ex -> println("**** ERROR: ${ex.message}") }
        }
    } else  {
        println("${directory.name} is not a valid directory")
        result = 40
    }
    return result
}

fun displayJars(jars: List<JarInfo>) {
    jars.forEach { jar ->
        displayJar(jar)
    }
}

fun displayJar(jar: JarInfo) {
    if (maxVersion == -1) {
        println("Name: ${jar.name}, min: ${jar.minVersion}, max: ${jar.maxVersion}, classes: ${jar.classes.size}")
    } else  if (jar.maxVersion > maxVersion) {
        println("Name: ${jar.name} exceeds max version ${maxVersion}")
    }
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

fun recurseDirectories(directory: File, jars: MutableList<JarInfo>): Int {
    var result = 0
    val directories = listDirectoriesRecursively(directory)
    debugMsg("Searching ${directories.size} directories")
    directories.forEach { directory ->
        result = processDirectory(directory, jars)
    }
    return result
}
