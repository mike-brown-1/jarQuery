package jarQuery

import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream

fun getJavaVersionFromClassFile(classFilePath: String): Int {
    DataInputStream(FileInputStream(File(classFilePath))).use { dis ->
// Read and check the magic number
        if (dis.readInt() != 0xCAFEBABE.toInt()) {
            throw IllegalArgumentException("Not a valid Java class file")
        }

        // Skip minor version
        dis.readUnsignedShort()

        // Read major version
        val majorVersion = dis.readUnsignedShort()
        if (majorVersion < 45 || majorVersion > 65) {
            throw IllegalArgumentException("unknown version number: ${majorVersion}")
        }
        return majorVersion - 44

//        // Map major version to Java version
//        return when (majorVersion) {
//            45 -> 1 // Java 1.1
//            46 -> 2 // Java 1.2
//            47 -> 3 // Java 1.3
//            48 -> 4 // Java 1.4
//            49 -> 5 // Java 5
//            50 -> 6 // Java 6
//            51 -> 7 // Java 7
//            52 -> 8 // Java 8
//            53 -> 9 // Java 9
//            54 -> 10 // Java 10
//            55 -> 11 // Java 11
//            56 -> 12 // Java 12
//            57 -> 13 // Java 13
//            58 -> 14 // Java 14
//            59 -> 15 // Java 15
//            60 -> 16 // Java 16
//            61 -> 17 // Java 17
//            62 -> 18 // Java 18
//            63 -> 19 // Java 19
//            64 -> 20 // Java 20
//            65 -> 21 // Java 21
//            else -> throw IllegalArgumentException("Unknown major version: $majorVersion")
//        }
    }
}
