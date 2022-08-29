package love.sola.laoshipp.hsk

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.events.CoroutineEventListener
import dev.minn.jda.ktx.interactions.components.getOption
import dev.minn.jda.ktx.messages.MessageCreate
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import java.awt.Color
import java.lang.Integer.min

object HskCommandListener : CoroutineEventListener {

    override suspend fun onEvent(event: GenericEvent) {
        if (event !is SlashCommandInteractionEvent) return
        if (!event.isFromGuild) return
        when (event.name) {
            "hsk", "tocfl" -> start(event)
            "hskstop", "tocflstop" -> stop(event)
        }
    }

    private suspend fun start(event: SlashCommandInteractionEvent) {
        val levelName = event.getOption<String>("level") ?: "1"
        val level = HskLevel.of(event.name, levelName)
        if (level == null) {
            event.reply("Level '${levelName}' does not exist!").setEphemeral(true).await()
            return
        }
        val rounds = min(100, event.getOption<Int>("rounds") ?: 3)
        val delay = min(32, event.getOption<Int>("delay") ?: 10)

        val options = HskGameOptions(level, rounds, delay)
        val channel = event.guildChannel
        if (HskGameManager.exists(channel)) {
            event.reply("You're already gaming!").setEphemeral(true).await()
            return
        }
        val game = HskGameManager.create(channel, options)
        event.reply(gameStartMessage(options, event.user)).await()

        game.run()
    }

    private suspend fun stop(event: SlashCommandInteractionEvent) {
        val channel = event.guildChannel
        val game = HskGameManager.get(channel)
        if (game == null) {
            event.reply("No game is currently running.").setEphemeral(true).await()
            return
        }
        if (!game.isCanceled()) {
            game.cancel()
            event.reply("${event.user.asMention} has ended the game. This will be the last round.").await()
        } else {
            event.reply("The game is already ended.").setEphemeral(true).await()
        }
    }

    private fun gameStartMessage(options: HskGameOptions, creator: User) = MessageCreate {
        embed {
            color = Color.CYAN.rgb
            title = "${options.level.title} Practice Initialized"
            description = """
                Game initiated by ${creator.asMention}, use /${options.level.command}stop to cancel.
                To play, type the pronunciation of the following characters in pinyin/zhuyin.
            """.trimIndent()
            field("Level", options.level.title)
            field("Rounds", options.rounds.toString())
            field("Timeout", "${options.delay} second(s)")
        }
    }
}
