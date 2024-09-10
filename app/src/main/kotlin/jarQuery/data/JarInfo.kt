package jarQuery.data

data class JarInfo(val name: String, var manifest: Map<String, String>, var minVersion: Int, var maxVersion: Int,
                   var classes: MutableList<ClassInfo> = mutableListOf<ClassInfo>()) {
}
