package love.sola.laoshipp.hsk

enum class HskLevel(val command: String, val level: String, val title: String, val simplified: Boolean) {

    HSK_1("hsk", "1", "HSK 1", true),
    HSK_2("hsk", "2", "HSK 2", true),
    HSK_3("hsk", "3", "HSK 3", true),
    HSK_4("hsk", "4", "HSK 4", true),
    HSK_5("hsk", "5", "HSK 5", true),
    HSK_6("hsk", "6", "HSK 6", true),

    TOCFL_1("tocfl", "1", "TOCFL 1", false),
    TOCFL_2("tocfl", "2", "TOCFL 2", false),
    TOCFL_3("tocfl", "3", "TOCFL 3", false),
    TOCFL_4("tocfl", "4", "TOCFL 4", false),
    TOCFL_5("tocfl", "5", "TOCFL 5", false),
    TOCFL_6("tocfl", "6", "TOCFL 6", false),
    TOCFL_7("tocfl", "7", "TOCFL 7", false),
    ;

    companion object {
        private data class Key(val command: String, val level: String) {
            constructor(level: HskLevel) : this(level.command, level.level)
        }

        private val mapping = HskLevel.values().associateBy(::Key)
        fun of(command: String, level: String): HskLevel? = mapping[Key(command, level)]
    }
}
