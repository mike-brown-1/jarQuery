package jarQuery

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
