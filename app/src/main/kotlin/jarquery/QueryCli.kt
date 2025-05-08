package jarquery

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.options.versionOption
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.clikt.parameters.types.int
import jarquery.utils.displayJar
import jarquery.utils.displayJars
import jarquery.utils.processDirectory
import jarquery.utils.processFile
import jarquery.utils.recurseDirectories
import kotlin.system.exitProcess

class QueryCli: CliktCommand(name = "jarquery") {

    val jarFile by option("-j", "--jar", help="Single jar file to process").file()
    val jarDirectory by option("-d", "--dir", help="Directory containing JAR files to process").file()
    val classes by option("-c", "--classes", help="Show information on classes in the jar file").flag(default = false)
    val manifest by option("-m", "--manifest", help="Show the manifest only").flag(default = false)
    val recurse by option("-r", "--recurse", help="With -d, search directory recursively").flag(default = false)
    val max by option("--max", help="Only display jar files with java version > max").int().default(-1)

    init {
        versionOption(Config.APPVERSION)
    }

    override fun run() {
        if (isValid()) {
            Config.classes = classes
            Config.recurse = recurse
            Config.manifest = manifest
            Config.maxVersion = max
        } else {
            exitProcess(10)
        }
        if (jarFile != null) {
            val processResult = processFile(jarFile!!)
            processResult.onSuccess { jar -> displayJar(jar) }
            processResult.onFailure { ex ->
                println("**** ERROR: ${ex.message}")
                exitProcess(50)
            }
        } else if (jarDirectory != null) {
            val processResult = if (Config.recurse) {
                recurseDirectories(jarDirectory!!)
            } else {
                processDirectory(jarDirectory!!)
            }
            processResult.onSuccess { jars -> displayJars(jars) }
            processResult.onFailure { ex ->
                println("**** ERROR: ${ex.message}")
                exitProcess(50)
            }
        }
    }

    fun isValid(): Boolean {
        var result = true
        if ((jarFile == null && jarDirectory == null) || (jarFile != null && jarDirectory != null)) {
            result = false
            println("You must provide either a jar File or a Directory")
        }
        return result
    }
}