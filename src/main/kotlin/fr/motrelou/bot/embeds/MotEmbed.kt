package fr.motrelou.bot.embeds

import dev.kord.common.Color
import dev.kord.core.entity.Member
import dev.kord.rest.builder.message.create.MessageCreateBuilder
import dev.kord.rest.builder.message.create.embed
import fr.motrelou.bot.api.MotApi
import fr.motrelou.bot.formatFrench
import kotlinx.datetime.Clock

fun MessageCreateBuilder.embedMot(mot: MotApi, user: Member){
	embed {
		author {
			name = user.displayName
			icon = user.avatar?.url
		}
		title = "Mot : ${mot.mot}"
		description = "Le ${mot.creation.formatFrench()}\n\n**Définitions :**"
		color = Color(0, 255, 0)
		mot.definitions.forEach {
			field("${it.index} : ${it.definition}", true) {
				"${user.displayName} le ${it.creation.formatFrench()}"
			}
		}
		timestamp = Clock.System.now()
		footer {
			text = "Bot mot relou"
		}
	}
}