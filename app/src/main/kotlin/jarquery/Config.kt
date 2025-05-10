package jarquery

/**
 * Holds the application configuration.
 *
 * Populated by [QueryCli] after parsing the command line arguments.
 */
object Config {
    var debug = false
    var manifest = false
    var classes = false
    var recurse = false
    const val APPVERSION = "1.2.0" // Updated by Gradle task
    var maxVersion = -1
}
