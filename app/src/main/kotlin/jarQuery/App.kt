package jarQuery
import jarQuery.data.JarInfo
import jarQuery.utils.displayJarInfo
import jarQuery.utils.error
import jarQuery.utils.processDirectory
import jarQuery.utils.processFile
import jarQuery.utils.recurseDirectories
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option

import java.io.File
import java.util.concurrent.Callable
import kotlin.math.max
import kotlin.system.exitProcess

@Command(name = "jarQuery", mixinStandardHelpOptions = true, version = ["jarQuery $appVersion"],
    description = ["Display information about one or more JAR files to STDOUT."])
class JarQuery : Callable<Int> {

    @Option(names = ["-j", "--jarFile"], arity = "0..1", paramLabel = "JAR file",
        description = ["the JAR file to process"])
    var jarFileOption: File? = null

    @Option(names = ["-d", "--directory"], arity = "0..1", paramLabel = "Directory",
        description = ["Directory containing JAR files to process"])
    var directoryOption: File? = null

    @Option(names = ["--debug"], description = ["Add detailed messages while processing request"])
    var debugOption = false

    @Option(names = ["-c", "--classes"], description = ["Show information on classes in the jar file"])
    var classesOption = false

    @Option(names = ["-m", "--manifest"], description = ["Display manifest attributes"])
    var manifestOption = false

    @Option(names = ["-r", "--recurse"], description = ["With -d, search directory recursively"])
    var recurseOption = false

    @Option(names = ["--max"], paramLabel = "max", description = ["Only display jar files with java version > max"])
    var maxVersionOption = -1

    override fun call(): Int {
        var result = 0
        debug = debugOption
        manifest = manifestOption
        classes = classesOption
        maxVersion = maxVersionOption
        recurse = recurseOption

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
                    if (recurse) {
                        result = recurseDirectories(dir, jars)
                    } else {
                        result = processDirectory(dir, jars)
                    }
                }
            }
        }
        displayJarInfo(jars)
        return result
    }
}

fun main(args: Array<String>) : Unit = exitProcess(CommandLine(JarQuery()).execute(*args))
