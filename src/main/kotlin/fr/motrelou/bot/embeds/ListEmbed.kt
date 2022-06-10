package fr.motrelou.bot.embeds

import dev.kord.common.Color
import dev.kord.common.entity.ButtonStyle
import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.message.modify.InteractionResponseModifyBuilder
import dev.kord.rest.builder.message.modify.UserMessageModifyBuilder
import dev.kord.rest.builder.message.modify.actionRow
import dev.kord.rest.builder.message.modify.embed
import fr.motrelou.bot.api.MotApi
import fr.motrelou.bot.formatFrench
import fr.motrelou.bot.kord
import fr.motrelou.bot.success

suspend fun InteractionResponseModifyBuilder.listEmbed(mots: List<MotApi>, guild: Snowflake, page: Long) {
	embed {
		author {
			name = "Bot Mot Relou"
		}
		title = "Liste des mots"
		description = "Page : ${page+1}"
		color = Color.success
		mots.forEach { mot ->
			val user = kord.getUser(Snowflake(mot.createur))!!
			val member = user.asMemberOrNull(guild)
			field(mot.mot) {
				(member?.displayName ?: user.username) + " le ${mot.creation.formatFrench()}"
			}
		}
	}
	actionRow {
		if(page > 0) {
			interactionButton(ButtonStyle.Primary, "prev$page") {
				label = "⬅️ Prev"
			}
		}
		interactionButton(ButtonStyle.Primary, "next$page") {
			label = "Next ➡️"
		}
	}
}

suspend fun UserMessageModifyBuilder.listEmbed(mots: List<MotApi>, guild: Snowflake, page: Long) {
	embed {
		author {
			name = "Bot Mot Relou"
		}
		title = "Liste des mots"
		description = "Page : ${page+1}"
		color = Color.success
		mots.forEach { mot ->
			val user = kord.getUser(Snowflake(mot.createur))!!
			val member = user.asMemberOrNull(guild)
			field(mot.mot) {
				(member?.displayName ?: user.username) + " le ${mot.creation.formatFrench()}"
			}
		}
	}
	actionRow {
		if(page > 0) {
			interactionButton(ButtonStyle.Primary, "prev$page") {
				label = "⬅️ Prev"
			}
		}
		interactionButton(ButtonStyle.Primary, "next$page") {
			label = "Next ➡️"
		}
	}
}