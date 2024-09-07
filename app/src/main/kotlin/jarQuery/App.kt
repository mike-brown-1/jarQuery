package jarQuery
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import picocli.CommandLine.Parameters
import sun.tools.jar.resources.jar

import java.io.File
import java.math.BigInteger
import java.nio.file.Files
import java.security.MessageDigest
import java.util.concurrent.Callable
import kotlin.system.exitProcess

@Command(name = "jarQuery", mixinStandardHelpOptions = true, version = ["jarQuery 0.2.0"],
    description = ["Display information about one or more JAR files to STDOUT."])
class JarQuery : Callable<Int> {

    @Option(names = ["-f", "--file"], arity = "0..1", paramLabel = "JAR file", description = ["the JAR file"])
    var jarFile: File? = null

    @Option(names = ["-d", "--directory"], arity = "0..1", paramLabel = "Directory",
        description = ["Directory containing JAR files"])
    var directory: File? = null

    fun error(message: String, code: Int) {
        println(message)
        System.exit(code)
    }

    override fun call(): Int {
        when {
            jarFile != null && directory != null -> error("Specify either file or directory, not both")
            jarFile == null && directory == null -> error("File or directory required")
            jarFile != null -> return processFile(jarFile)
            else -> return processDirectory(directory)
        }
    }
}

fun main(args: Array<String>) : Unit = exitProcess(CommandLine(JarQuery()).execute(*args))
/*
fun main(args: Array<String>) {
    if (args.size == 0) {
        throw IllegalArgumentException("Must provide class file name as an argument")
    }

    println("processing: ${args[0]}")
    val jarFile = isValidFile(args[0])
    if (jarFile == null) {
        throw IllegalArgumentException("${args[0]} is not a valid file")
    }

    println("----manifest:")
    val manifest = jarFile.manifest
    for ((key, value) in manifest.mainAttributes) {
        println("${key}: $value")
    }

    println("---- jar contents:")
    jarFile.stream().forEach { entry ->
        println(entry)
    }
}
*/
