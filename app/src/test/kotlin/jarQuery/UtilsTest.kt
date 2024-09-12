package jarQuery

import jarQuery.utils.getJarFiles
import jarQuery.utils.getJavaVersionFromStream
import org.junit.jupiter.api.Test
import java.io.File
import java.lang.IllegalArgumentException
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
}