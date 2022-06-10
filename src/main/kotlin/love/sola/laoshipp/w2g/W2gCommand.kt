package love.sola.laoshipp.w2g

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.events.CoroutineEventListener
import dev.minn.jda.ktx.interactions.components.getOption
import net.dv8tion.jda.api.entities.GuildChannel
import net.dv8tion.jda.api.entities.VoiceChannel
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

object W2gCommand : CoroutineEventListener {

    override suspend fun onEvent(event: GenericEvent) {
        if (event !is SlashCommandInteractionEvent) return
        if (!event.isFromGuild) return
        if (event.name != "youtube") return
        val vc = voiceChannelFromOption(event) ?: userVoiceChannel(event)
        if (vc == null) {
            event.reply("Please join a VC first!").setEphemeral(true).await()
            return
        }
        val invite = vc.createInvite().setTargetApplication(880218394199220334).await()
        event.reply("https://discord.gg/${invite.code}").await()
    }

    private fun voiceChannelFromOption(event: SlashCommandInteractionEvent): VoiceChannel? {
        val channel = event.getOption<GuildChannel?>("channel")
        return channel as? VoiceChannel
    }

    private fun userVoiceChannel(event: SlashCommandInteractionEvent): VoiceChannel? {
        return event.member?.voiceState?.channel as? VoiceChannel
    }
}
