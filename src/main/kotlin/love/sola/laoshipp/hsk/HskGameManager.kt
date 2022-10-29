package love.sola.laoshipp.hsk

import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import java.util.concurrent.ConcurrentHashMap

object HskGameManager {

    private val sessions = ConcurrentHashMap<HskGameSessionId, HskGame>()

    fun exists(channel: GuildMessageChannel) = sessions.contains(HskGameSessionId(channel))

    fun get(channel: GuildMessageChannel) = sessions[HskGameSessionId(channel)]

    fun create(channel: GuildMessageChannel, options: HskGameOptions): HskGame {
        val id = HskGameSessionId(channel)
        val game = HskGame(channel, options)
        val prev = sessions.putIfAbsent(id, game)
        if (prev != null) {
            throw IllegalStateException("Game session already exists!")
        }
        return game
    }

    fun remove(channel: GuildMessageChannel) {
        val id = HskGameSessionId(channel)
        val game = sessions[id] ?: return
        if (game.isRunning()) {
            throw IllegalStateException("The game is still running!")
        }
        sessions.remove(id)
    }
}
