package love.sola.laoshipp.hsk

import net.dv8tion.jda.api.entities.GuildChannel

data class HskGameSessionId(val guildId: Long, val channelId: Long) {
    constructor(guildChannel: GuildChannel) : this(
        guildChannel.guild.idLong,
        guildChannel.idLong
    )
}
