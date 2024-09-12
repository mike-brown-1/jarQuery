package jarQuery
import jarQuery.data.JarInfo
import jarQuery.utils.displayJarInfo
import jarQuery.utils.error
import jarQuery.utils.processDirectory
import jarQuery.utils.processFile
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option

import java.io.File
import java.util.concurrent.Callable
import kotlin.system.exitProcess

@Command(name = "jarQuery", mixinStandardHelpOptions = true, version = ["jarQuery $appVersion"],
    description = ["Display information about one or more JAR files to STDOUT."])
class JarQuery : Callable<Int> {

    @Option(names = ["-f", "--file"], arity = "0..1", paramLabel = "JAR file", description = ["the JAR file"])
    var jarFileOption: File? = null

    @Option(names = ["-d", "--directory"], arity = "0..1", paramLabel = "Directory",
        description = ["Directory containing JAR files"])
    var directoryOption: File? = null

    @Option(names = ["--debug"], description = ["Add detailed messages while processing request"])
    var debugOption = false

    @Option(names = ["-m", "--manifest"], description = ["Display manifest attributes"])
    var manifestOption = false

    override fun call(): Int {
        var result = 0
        debug = debugOption
        manifest = manifestOption
        val jars: MutableList<JarInfo> = mutableListOf()

        when {
            jarFileOption != null && directoryOption != null -> error("Specify either file or directory, not both", 10)
            jarFileOption == null && directoryOption == null -> error("File or directory required", 20)
            jarFileOption != null -> {
                // work around "mutable property that could be mutated concurrently" error
                val jf = jarFileOption
                if (jf != null) {
                    result = processFile(jf, jars)
                }
            }
            else -> {
                val dir = directoryOption
                if (dir != null) {
                    result = processDirectory(dir, jars)
                }
            }
        }
        displayJarInfo(jars)
        return result
    }
}

fun main(args: Array<String>) : Unit = exitProcess(CommandLine(JarQuery()).execute(*args))
