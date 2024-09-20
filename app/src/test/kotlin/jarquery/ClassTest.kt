package jarquery

import jarquery.utils.getJavaVersionFromStream
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class ClassTest {

    @Test
    fun getJavaVersion() {
        val classFile = File("./build/classes/kotlin/main/jarquery/AppKt.class")
        val result = getJavaVersionFromStream(classFile.inputStream())
        assertTrue(result.isSuccess)
        println("target version: ${result.getOrNull()}")
    }

    @Test
    fun testInvalidClassFile() {
        val  result = getJavaVersionFromStream(File("/home/mike/projects/jarquery/README.md").inputStream())
        assertTrue(result.isFailure)
        println("Failure: ${result.exceptionOrNull()?.message}")
    }
}