package love.sola.laoshipp.activity

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.events.CoroutineEventListener
import dev.minn.jda.ktx.interactions.components.getOption
import net.dv8tion.jda.api.entities.GuildChannel
import net.dv8tion.jda.api.entities.VoiceChannel
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

object ActivityCommand : CoroutineEventListener {

    private val applications = mapOf(
        "youtube" to 880218394199220334,
        "youtube-dev" to 880218832743055411,
        "sketch" to 902271654783242291,
        "chess" to 832012774040141894,
        "puttparty" to 945737671223947305
    )

    override suspend fun onEvent(event: GenericEvent) {
        if (event !is SlashCommandInteractionEvent) return
        if (!event.isFromGuild) return
        val applicationId = applications[event.name] ?: return
        val vc = voiceChannelFromOption(event) ?: userVoiceChannel(event)
        if (vc == null) {
            event.reply("Please join a VC first!").setEphemeral(true).await()
            return
        }
        val invite = vc.createInvite().setTargetApplication(applicationId).await()
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
