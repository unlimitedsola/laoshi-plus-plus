package love.sola.laoshipp.hsk

import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel

data class HskGameSessionId(val guildId: Long, val channelId: Long) {
    constructor(channel: GuildMessageChannel) : this(
        channel.guild.idLong,
        channel.idLong
    )
}
