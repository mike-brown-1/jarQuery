package jarQuery

import jarQuery.utils.getJavaVersionFromStream
import java.lang.IllegalArgumentException
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ClassTest {

    @Test
    fun getJavaVersion() {
        val classFileName = "./build/classes/kotlin/main/jarQuery/AppKt.class"
        println("target version: ${getJavaVersionFromStream(classFileName)}")
        assertTrue(true)
    }

    @Test
    fun testInvalidClassFile() {
        assertFailsWith<IllegalArgumentException> {
            getJavaVersionFromStream("/home/mike/projects/jarQuery/README.md")
        }
    }
}