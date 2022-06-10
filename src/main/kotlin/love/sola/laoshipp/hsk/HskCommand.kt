package love.sola.laoshipp.hsk

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.events.CoroutineEventListener
import dev.minn.jda.ktx.messages.Message
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import java.awt.Color

object HskCommand : CoroutineEventListener {

    override suspend fun onEvent(event: GenericEvent) {
        if (event !is SlashCommandInteractionEvent) return
        if (!event.isFromGuild) return
        when (event.name) {
            "hsk" -> start(event)
            "hskstop" -> stop(event)
        }
    }

    private suspend fun start(event: SlashCommandInteractionEvent) {
        val options = HskGameOptions(event)
        if (options.level !in Dictionary) {
            event.reply("Level '${options.level}' does not exist!").setEphemeral(true).await()
            return
        }

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

    private fun gameStartMessage(options: HskGameOptions, creator: User) = Message {
        embed {
            color = Color.CYAN.rgb
            title = "HSK ${options.level} Practice Initialized"
            description = """
                Game initiated by ${creator.asMention}, use /hskstop to cancel.
                To play, type the translation for the following character in pinyin or english.
            """.trimIndent()
            field("Level", options.level)
            field("Rounds", options.rounds.toString())
            field("Timeout", "${options.delay} second(s)")
        }
    }
}
