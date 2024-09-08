package jarQuery

import org.junit.jupiter.api.Test
import java.io.File
import java.io.FilenameFilter
import java.lang.IllegalArgumentException
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JarTest {

    @Test
    fun testInvalidJarFile() {
        val f = File("foo")
        assertFalse(isValidFile(f))
    }

    @Test
    fun testValidJarFile() {
        val buildLibsDir = File("app/build/libs")
        val jarFilter = FilenameFilter { _, name ->
            name.endsWith(".jar")
        }
        val jarFiles = buildLibsDir.listFiles(jarFilter)
        if (jarFiles != null) {
            assertTrue(isValidFile(jarFiles[0]))
        }
    }

    @Test
    fun testProcessFile() {
        val buildLibsDir = File("app/build/libs")
        val jarFilter = FilenameFilter { _, name ->
            name.endsWith(".jar")
        }
        val jarFiles = buildLibsDir.listFiles(jarFilter)
        if (jarFiles != null) {
            val result = processFile(jarFiles[0])
            assertTrue(result == 0)
        }
    }
}