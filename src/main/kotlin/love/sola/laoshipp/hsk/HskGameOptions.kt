package love.sola.laoshipp.hsk

import dev.minn.jda.ktx.interactions.components.getOption
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import java.lang.Integer.min

data class HskGameOptions(
    val level: String,
    val rounds: Int,
    val delay: Int
) {
    constructor(e: SlashCommandInteractionEvent) : this(
        e.getOption<String>("level") ?: "1",
        min(100, e.getOption<Int>("rounds") ?: 3),
        min(32, e.getOption<Int>("delay") ?: 10)
    )
}
