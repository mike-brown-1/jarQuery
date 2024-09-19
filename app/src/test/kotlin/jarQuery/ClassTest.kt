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
        val result = getJavaVersionFromStream(classFile.inputStream())
        assertTrue(result.isSuccess)
        println("target version: ${result.getOrNull()}")
    }

    @Test
    fun testInvalidClassFile() {
        val  result = getJavaVersionFromStream(File("/home/mike/projects/jarQuery/README.md").inputStream())
        assertTrue(result.isFailure)
        println("Failure: ${result.exceptionOrNull()?.message}")
    }
}