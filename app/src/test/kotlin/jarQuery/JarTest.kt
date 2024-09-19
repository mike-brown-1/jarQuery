package jarQuery

import jarQuery.data.JarInfo
import jarQuery.utils.isValidFile
import jarQuery.utils.processFile
import jarQuery.utils.recurseDirectories
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FilenameFilter
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
        val buildLibsDir = File("build/libs")
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
        val buildLibsDir = File("build/libs")
        val jarFilter = FilenameFilter { _, name ->
            name.endsWith(".jar")
        }
        val jarFiles = buildLibsDir.listFiles(jarFilter)
        if (jarFiles != null) {
            val jars: MutableList<JarInfo> = mutableListOf()
            val result = processFile(jarFiles[0], jars)
            assertTrue(result == 0)
        }
    }

    @Test
    fun testRecurse() {
        val buildLibsDir = File("build/libs")
        val jars: MutableList<JarInfo> = mutableListOf()
        val result = recurseDirectories(buildLibsDir, jars)
        assertTrue(result == 0)
        assertTrue(jars.isNotEmpty())
    }
}