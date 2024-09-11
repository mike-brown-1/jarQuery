package jarQuery

import jarQuery.utils.getJavaVersionFromStream
import java.io.File
import java.lang.IllegalArgumentException
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ClassTest {

    @Test
    fun getJavaVersion() {
        val classFile = File("./build/classes/kotlin/main/jarQuery/AppKt.class")
        println("target version: ${getJavaVersionFromStream(classFile.inputStream())}")
        assertTrue(true)
    }

    @Test
    fun testInvalidClassFile() {
        assertFailsWith<IllegalArgumentException> {
            getJavaVersionFromStream(File("/home/mike/projects/jarQuery/README.md").inputStream())
        }
    }
}