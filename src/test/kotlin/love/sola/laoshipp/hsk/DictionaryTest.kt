package love.sola.laoshipp.hsk

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

internal class DictionaryTest {

    @Test
    fun test() {
        val dict = Dictionary[HskLevel.HSK_1]
        assertTrue(dict.isNotEmpty())
    }
}
