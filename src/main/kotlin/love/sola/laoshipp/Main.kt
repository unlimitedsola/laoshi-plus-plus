package love.sola.laoshipp

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.interactions.commands.option
import dev.minn.jda.ktx.interactions.commands.slash
import dev.minn.jda.ktx.interactions.commands.updateCommands
import dev.minn.jda.ktx.jdabuilder.cache
import dev.minn.jda.ktx.jdabuilder.intents
import dev.minn.jda.ktx.jdabuilder.light
import love.sola.laoshipp.activity.ActivityCommand
import love.sola.laoshipp.hsk.HskCommandListener
import love.sola.laoshipp.hsk.HskGameEventListener
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.GuildChannel
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.cache.CacheFlag

suspend fun main() {
    val token = System.getenv("DISCORD_TOKEN") ?: throw IllegalArgumentException("DISCORD_TOKEN is not defined.")
    val jda = light(token, enableCoroutines = true) {
        setMemberCachePolicy(MemberCachePolicy.DEFAULT)
        intents += GatewayIntent.GUILD_VOICE_STATES
        cache += CacheFlag.VOICE_STATE
        addEventListeners(HskCommandListener)
        addEventListeners(HskGameEventListener)
        addEventListeners(ActivityCommand)
    }

    jda.updateCommands {
        slash("hsk", "Start the game!") {
            option<Int>("level", "Currently we have 1-6 levels.", false)
            option<Int>("rounds", "How many rounds you want to play.", false)
            option<Int>("delay", "The timeout for each round (in seconds).", false)
        }
        slash("tocfl", "Start the game!") {
            option<Int>("level", "Currently we have 1-7 levels.", false)
            option<Int>("rounds", "How many rounds you want to play.", false)
            option<Int>("delay", "The timeout for each round (in seconds).", false)
        }
        slash("hskstop", "End the current running game.")

        slash("youtube", "Watch YouTube together!") {
            option<GuildChannel>("channel", "The voice channel you want to watch in.", false)
        }
        slash("sketch", "Play Sketch Heads!") {
            option<GuildChannel>("channel", "The voice channel you want to play in.", false)
        }
    }.await()

    jda.presence.setPresence(OnlineStatus.ONLINE, Activity.competing("/hsk [1-6]"))
}
