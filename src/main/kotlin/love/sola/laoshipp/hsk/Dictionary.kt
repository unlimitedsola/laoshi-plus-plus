package love.sola.laoshipp.hsk

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object Dictionary {

    private val levels: Map<HskLevel, List<Word>> by lazy { load() }

    private fun load(): Map<HskLevel, List<Word>> {
        return HskLevel.values().associateWith { level ->
            val json = this.javaClass.getResource("/levels/${level.name.lowercase()}.json")?.readText()
                ?: throw NoSuchElementException("Dictionary for level $level can't be found.")
            Json.decodeFromString(json)
        }
    }

    operator fun contains(level: HskLevel) = levels.contains(level)
    operator fun get(level: HskLevel) =
        levels[level] ?: throw IllegalArgumentException("Level '$level' does not exist!")
}

@Serializable
data class Word(
    val chs: String,
    val cht: String,
    val pinyin: List<String> = emptyList(),
    val zhuyin: String? = null,
    val translation: List<String> = emptyList()
)
