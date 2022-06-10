package love.sola.laoshipp.hsk

import net.dv8tion.jda.api.entities.User

data class HskPlayer(
    val id: Long,
    val name: String
) {
    constructor(user: User) : this(
        user.idLong,
        user.name
    )
}
