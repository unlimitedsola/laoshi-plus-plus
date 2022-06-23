package love.sola.laoshipp.hsk

enum class HskLevel(val command: String, val level: String, val title: String) {

    HSK_1("hsk", "1", "HSK 1"),
    HSK_2("hsk", "2", "HSK 2"),
    HSK_3("hsk", "3", "HSK 3"),
    HSK_4("hsk", "4", "HSK 4"),
    HSK_5("hsk", "5", "HSK 5"),
    HSK_6("hsk", "6", "HSK 6"),

    TOCFL_1("tocfl", "1", "TOCFL 1"),
    TOCFL_2("tocfl", "2", "TOCFL 2"),
    TOCFL_3("tocfl", "3", "TOCFL 3"),
    TOCFL_4("tocfl", "4", "TOCFL 4"),
    TOCFL_5("tocfl", "5", "TOCFL 5"),
    TOCFL_6("tocfl", "6", "TOCFL 6"),
    TOCFL_7("tocfl", "7", "TOCFL 7"),
    ;

    companion object {
        private data class Key(val command: String, val level: String) {
            constructor(level: HskLevel) : this(level.command, level.level)
        }

        private val mapping = HskLevel.values().associateBy(::Key)
        fun of(command: String, level: String): HskLevel? = mapping[Key(command, level)]
    }
}
