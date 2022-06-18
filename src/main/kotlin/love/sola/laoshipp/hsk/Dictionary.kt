package love.sola.laoshipp.hsk

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object Dictionary {
    private val LEVELS = listOf(
        "1", "2", "3", "4", "5", "6"
    )

    private val levels: Map<String, List<Word>> by lazy { load() }

    private fun load(): Map<String, List<Word>> {
        return LEVELS.associateWith { name ->
            val json = this.javaClass.getResource("/levels/$name.json")?.readText() ?: "{}"
            Json.decodeFromString(json)
        }
    }

    operator fun contains(name: String) = levels.contains(name)
    operator fun get(name: String) = levels[name] ?: throw IllegalArgumentException("Level '$name' does not exist!")
}

@Serializable
data class Word(
    val chs: String,
    val cht: String,
    val pinyin: List<String> = emptyList(),
    val zhuyin: String? = null,
    val translation: List<String> = emptyList()
)
