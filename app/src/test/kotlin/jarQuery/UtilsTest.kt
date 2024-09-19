package jarQuery

import jarQuery.utils.getJarFiles
import jarQuery.utils.getJavaVersionFromStream
import jarQuery.utils.listDirectoriesRecursively
import org.junit.jupiter.api.Test
import java.io.File
import java.lang.IllegalArgumentException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class UtilsTest {

    @Test
    fun getJarsFromDirectory() {
        val dir = File("../jars")
        val jars = getJarFiles(dir)
        assertTrue(jars.size > 0, "should have a couple of jars")
    }

    @Test
    fun maps() {
        val m = mutableMapOf<String, String>()
        m["one"] = "1"
        for ((key, value) in m) {
            println("$key: $value")
        }

    }

    @Test
    fun dateTime() {
        val currentTime = LocalDateTime.now()
        println("currentTime: $currentTime")
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
        println("formatted: ${currentTime.format(formatter)}")
    }

    @Test
    fun listDirectories() {
        val directories = listDirectoriesRecursively(File("."))
        assertTrue(directories.isNotEmpty())
    }
}
