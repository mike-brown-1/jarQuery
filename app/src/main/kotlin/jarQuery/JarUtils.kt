package jarQuery

import java.io.File
import java.util.jar.JarFile

fun isValidFile(fileName: String): JarFile? {
    val file = File(fileName)
    return if (file.exists() && file.isFile()) {
        return JarFile(file)
    } else {
        return null
    }
}
