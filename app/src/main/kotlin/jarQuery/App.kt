package jarQuery
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option

import java.io.File
import java.util.concurrent.Callable
import kotlin.system.exitProcess

@Command(name = "jarQuery", mixinStandardHelpOptions = true, version = ["jarQuery 0.2.0"],
    description = ["Display information about one or more JAR files to STDOUT."])
class JarQuery : Callable<Int> {

    @Option(names = ["-f", "--file"], arity = "0..1", paramLabel = "JAR file", description = ["the JAR file"])
    public var jarFileOption: File? = null

    @Option(names = ["-d", "--directory"], arity = "0..1", paramLabel = "Directory",
        description = ["Directory containing JAR files"])
    var directoryOption: File? = null

    @Option(names = ["--debug"], description = ["Add detailed messages while processing request"])
    var debugOption = false

    override fun call(): Int {
        var result = 0
        debug = debugOption

        when {
            jarFileOption != null && directoryOption != null -> error("Specify either file or directory, not both", 10)
            jarFileOption == null && directoryOption == null -> error("File or directory required", 20)
            jarFileOption != null -> {
                // work around "mutable property that could be mutated concurrently" error
                val jf = jarFileOption
                if (jf != null) {
                    result = processFile(jf)
                }
            }
            else -> {
                val dir = directoryOption
                if (dir != null) {
                    result =  processDirectory(dir)
                }
            }
        }
        return result
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
