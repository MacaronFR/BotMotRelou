package fr.motrelou.bot.embeds

import dev.kord.common.Color
import dev.kord.common.DiscordTimestampStyle
import dev.kord.common.entity.ButtonStyle
import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.message.modify.*
import fr.motrelou.bot.api.MotApi
import fr.motrelou.bot.kord
import fr.motrelou.bot.success
import fr.motrelou.bot.toMessageFormat
import kotlinx.datetime.Clock

suspend fun MessageModifyBuilder.listEmbed(mots: List<MotApi>, guild: Snowflake, page: Long, prefix: String = "") {
	embed {
		author {
			name = "Bot Mot Relou"
			icon = kord.getSelf().avatar?.url
		}
		title = "Liste des mots"
		description = "Page : ${page+1}"
		color = Color.success
		timestamp = Clock.System.now()
		mots.forEach { mot ->
			val user = kord.getUser(Snowflake(mot.createur))!!
			val member = user.asMemberOrNull(guild)
			field(mot.mot) {
				(member?.displayName ?: user.username) + " le ${mot.creation.toMessageFormat(DiscordTimestampStyle.LongDate)}"
			}
		}
	}
	actionRow {
		if(page > 0) {
			interactionButton(ButtonStyle.Primary, "${prefix}prev$page") {
				label = "⬅️ Prev"
			}
		}
		interactionButton(ButtonStyle.Primary, "${prefix}next$page") {
			label = "Next ➡️"
		}
	}
}