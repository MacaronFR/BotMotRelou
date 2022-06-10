package fr.motrelou.bot.embeds

import dev.kord.common.Color
import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.message.create.MessageCreateBuilder
import dev.kord.rest.builder.message.create.embed
import fr.motrelou.bot.api.MotApi
import fr.motrelou.bot.formatFrench
import fr.motrelou.bot.getUserInGuild
import fr.motrelou.bot.kord
import fr.motrelou.bot.success
import kotlinx.datetime.Clock

suspend fun MessageCreateBuilder.embedMot(mot: MotApi, guild: Snowflake){
	val user = kord.getUser(Snowflake(mot.createur))!!
	val member = kord.getUserInGuild(user.id, guild)
	embed {
		author {
			name = member?.displayName ?: user.username
			icon = member?.memberAvatar?.url ?: user.avatar?.url
		}
		title = "Mot : ${mot.mot}"
		description = "Le ${mot.creation.formatFrench()}\n\n**Définitions :**"
		color = Color.success
		mot.definitions.forEach {
			field("${it.index} : ${it.definition}") {
				(kord.getUserInGuild(Snowflake(it.createur), guild)?.displayName ?: user.username ) + " le ${it.creation.formatFrench()}"
			}
		}
		timestamp = Clock.System.now()
		footer {
			text = "Bot mot relou"
		}
	}
}