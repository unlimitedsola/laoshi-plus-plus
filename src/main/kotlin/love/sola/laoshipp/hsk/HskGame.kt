package love.sola.laoshipp.hsk

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.messages.InlineEmbed
import dev.minn.jda.ktx.messages.Message
import kotlinx.coroutines.delay
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.GuildMessageChannel
import java.awt.Color
import java.net.URLEncoder
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.time.Duration.Companion.seconds

class HskGame(
    private val channel: GuildMessageChannel,
    private val options: HskGameOptions
) {
    private val started = AtomicBoolean(false)
    private val running = AtomicBoolean(false)
    private val canceled = AtomicBoolean(false)

    fun isRunning() = running.get()
    fun cancel() = canceled.set(true)
    fun isCanceled() = canceled.get()

    private var round = 0
    private lateinit var word: Word

    private val scores = mutableMapOf<HskPlayer, Int>()
    private val answers = ConcurrentHashMap<HskPlayer, String>()

    suspend fun run() {
        if (!started.compareAndSet(false, true)) {
            throw IllegalStateException("That game already started!")
        }
        running.set(true)
        try {
            repeat(options.rounds) {
                delay(3.seconds) // brief period
                round()
                delay(options.delay.seconds)
                judge()
                if (isCanceled()) return
            }
        } finally {
            running.set(false)
            HskGameManager.remove(channel)
        }
    }

    private suspend fun round() {
        round++
        word = Dictionary[options.level].random()
        channel.sendMessage(roundStartMessage()).await()
    }

    fun updateAnswer(player: HskPlayer, answer: String) {
        answers[player] = answer
    }

    private suspend fun judge() {
        val result = answers.mapValues { (_, v) -> word.judge(v) }
        result.entries.forEach { (p, a) ->
            scores.compute(p) { _, v -> (v ?: 0) + if (a) 1 else 0 }
        }
        channel.sendMessage(roundEndMessage(result)).await()
        answers.clear()
    }

    private fun Word.judge(answer: String) = answer in pinyin || answer == zhuyin

    private fun roundStartMessage() = Message {
        embed {
            title = "Round #$round / ${options.rounds}"
            description =
                """To play, type the pronunciation of the following characters in pinyin/zhuyin."""
            color = Color.GREEN.rgb
            renderCharacters()
        }
    }

    private fun roundEndMessage(results: Map<HskPlayer, Boolean>) = Message {
        val lastRound = round == options.rounds || isCanceled()
        embed {
            title = "Round #$round - Complete"
            color = if (lastRound) Color.RED.rgb else Color.YELLOW.rgb

            renderCharacters()
            renderPronunciation()
            renderTranslation()

            field {
                name = "Results"
                value = scores.entries.sortedByDescending(Map.Entry<HskPlayer, Int>::value)
                    .joinToString(separator = "\n") { (k, v) ->
                        val icon = when (results[k]) {
                            true -> ":white_check_mark:"
                            false -> ":negative_squared_cross_mark:"
                            null -> ":white_large_square:"
                        }
                        "$icon [$v] ${k.name}"
                    }
                inline = false
            }
            footer {
                name = if (lastRound) {
                    "This was the last round."
                } else {
                    "Next round will begin shortly; use /${options.level.command}stop to quit."
                }
            }
        }
    }

    private fun InlineEmbed.renderCharacters() {
        field {
            if (options.level.simplified) {
                name = "Characters (Simplified/Traditional)"
                value = "${word.chs} / ${word.cht}"
            } else {
                name = "Characters (Traditional/Simplified)"
                value = "${word.cht} / ${word.chs}"
            }
        }
    }

    private fun InlineEmbed.renderPronunciation() {
        fun InlineEmbed.pinyin() {
            if (word.pinyin.isNotEmpty()) {
                field {
                    name = "Pinyin"
                    value = word.pinyin.joinToString(" / ")
                }
            }
        }

        fun InlineEmbed.zhuyin() {
            if (word.zhuyin != null) {
                field {
                    name = "Zhuyin"
                    value = word.zhuyin ?: EmbedBuilder.ZERO_WIDTH_SPACE
                }
            }
        }

        if (options.level.simplified) {
            zhuyin()
            pinyin()
        } else {
            zhuyin()
            pinyin()
        }
    }

    private fun InlineEmbed.renderTranslation() {
        if (word.translation.isNotEmpty()) {
            field {
                name = "Translations"
                value = word.translation.joinToString("\n")
                inline = false
            }
        }
    }

    private fun InlineEmbed.renderTools() {
        field {
            name = "Tools"
            val link = if (options.level.simplified) {
                naverLink(word.chs)
            } else {
                moedictLink(word.cht)
            }
            value = "[Example Sentences](${link})"
        }
    }
}

private fun naverLink(word: String): String {
    return "https://dict.naver.com/linedict/zhendict/dict.html#/cnen/example?query=" +
            URLEncoder.encode(word, Charsets.UTF_8)
}

private fun moedictLink(word: String): String {
    return "https://www.moedict.tw/~" +
            URLEncoder.encode(word, Charsets.UTF_8)
}
