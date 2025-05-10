package jarquery.utils

import jarquery.Config
import jarquery.data.ClassInfo
import jarquery.data.JarInfo
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.jar.JarFile
import java.util.zip.ZipException
import kotlin.collections.component1
import kotlin.collections.component2

/**
 * Determines if a file object is indeed a file and it exists
 * @param file the file to check
 * @return true if it meets the criteria
 */
fun isValidFile(file: File): Boolean {
    return file.exists() && file.isFile()
}

/**
 * Determines if a file object is indeed a directory and it exists
 * @param dir the directory to check
 * @return true if it meets the criteria
 */
fun isValidDirectory(dir: File): Boolean {
    return dir.exists() && dir.isDirectory()
}

fun processFile(file: File): Result<JarInfo> {
    debugMsg("processing file: ${file.name}")
    var result: Result<JarInfo>
    if (!isValidFile(file)) {
        result = Result.failure(Exception("${file.name} is not a file or does not exist"))
    } else {
        try {
            val jFile = JarFile(file)
            val jarInfo = JarInfo(jFile.name, mutableMapOf<String, String>(), 99, 0, mutableListOf<ClassInfo>())

            if (Config.manifest) {
                val manifest = jFile.manifest
                for ((key, value) in manifest.mainAttributes) {
                    jarInfo.manifest["$key"] = value.toString()
                }
            }

            var entryCount = 0
            jFile.stream().forEach { entry ->
                if (!entry.isDirectory && entry.name.endsWith(".class")
                    && !entry.name.startsWith("META-INF")) {
                    val processResult = getJavaVersionFromStream(jFile.getInputStream(entry))
                    processResult.onFailure { ex ->
                        println("**** ERROR: ${ex.message} for class: ${entry.name} in: ${jFile.name}")
                    }
                    processResult.onSuccess { ver ->
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
            result = Result.success(jarInfo)
        } catch (e: ZipException) {
            result = Result.failure(Exception("Not a valid jar file: ${e.message}"))
        }
    }
    return result
}

fun processDirectory(directory: File): Result<MutableList<JarInfo>> {
    if (isValidDirectory(directory)) {
        debugMsg("processing directory: ${directory.absolutePath}")
        val jarFiles = getJarFiles(directory)
        val jars: MutableList<JarInfo> = mutableListOf()
        jarFiles.forEach { jar ->
            val result = processFile(jar)
            result.onSuccess { jarInfo -> jars.add(jarInfo) }
            result.onFailure { ex -> println("**** ERROR: ${ex.message}") }
        }
        return Result.success(jars)
    } else  {
        return Result.failure(Exception("${directory.name} is not a valid directory"))
    }
}

fun displayJars(jars: List<JarInfo>) {
    jars.forEach { jar ->
        displayJar(jar)
    }
}

fun displayJar(jar: JarInfo) {
    if (Config.maxVersion == -1) {
        println("Name: ${jar.name}, min: ${jar.minVersion}, max: ${jar.maxVersion}, classes: ${jar.classes.size}")
    } else  if (jar.maxVersion > Config.maxVersion) {
        println("Name: ${jar.name} exceeds max version ${Config.maxVersion}")
    }
    if (Config.manifest) {
        println("    Manifest:")
        for ((key, value) in jar.manifest) {
            println("        $key: $value")
        }
        println()
    }
    if (Config.classes) {
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
        jar.classes.forEach { clazz ->
            println("    name: ${clazz.name}, ver: ${clazz.version}, size: ${clazz.size}, "
                    + "modified: ${clazz.modified.format(formatter)}")

        }
        println("")
    }
}

fun recurseDirectories(directory: File): Result<MutableList<JarInfo>> {
    val directories = listDirectoriesRecursively(directory)
    val jars: MutableList<JarInfo> = mutableListOf()
    debugMsg("Searching ${directories.size} directories")
    directories.forEach { theDirectory ->
        val result = processDirectory(theDirectory)
        result.onSuccess { jarInfo -> jars.addAll(jarInfo) }
    }
    return Result.success(jars)
}
