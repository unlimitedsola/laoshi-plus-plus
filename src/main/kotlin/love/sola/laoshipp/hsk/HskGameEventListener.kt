package love.sola.laoshipp.hsk

import dev.minn.jda.ktx.events.CoroutineEventListener
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

object HskGameEventListener : CoroutineEventListener {

    override suspend fun onEvent(event: GenericEvent) {
        when (event) {
            is MessageReceivedEvent -> onChatMessage(event)
        }
    }

    private fun onChatMessage(event: MessageReceivedEvent) {
        if (!event.isFromGuild) return
        if (event.author.isSystem) return
        if (event.author.isBot) return
        val channel = event.guildChannel
        val game = HskGameManager.get(channel) ?: return
        val player = HskPlayer(event.author)
        val answer = event.message.contentStripped.trim().lowercase()
        game.updateAnswer(player, answer)
    }
}
