package jarquery.data

data class JarInfo(val name: String, val manifest: MutableMap<String, String> = mutableMapOf<String, String>(),
                   var minVersion: Int, var maxVersion: Int,
                   var classes: MutableList<ClassInfo> = mutableListOf<ClassInfo>())
