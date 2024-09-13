package jarQuery.data

import java.time.LocalDateTime

data class ClassInfo(val name: String, val version: Int, val size: Long, val modified: LocalDateTime) {
}

// TODO get last modified at local date time
