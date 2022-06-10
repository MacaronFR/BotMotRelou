package fr.motrelou.bot.embeds

import dev.kord.common.Color
import dev.kord.core.entity.Member
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.create.MessageCreateBuilder
import dev.kord.rest.builder.message.create.embed
import dev.kord.rest.builder.message.modify.MessageModifyBuilder
import dev.kord.rest.builder.message.modify.embed
import fr.motrelou.bot.error
import kotlinx.datetime.Clock

fun MessageCreateBuilder.embedErreur(message: String, user: Member, description: String, color: Color) {
	embed {
		buildErreur(message, user, description, color)
	}
}

fun MessageModifyBuilder.embedErreur(message: String, user: Member, description: String, color: Color) {
	embed {
		buildErreur(message, user, description, color)
	}
}

private fun EmbedBuilder.buildErreur(message: String, user: Member, description: String, color: Color){
	author {
		name = user.displayName
		icon = user.avatar?.url
	}
	title = "Erreur : $message"
	this.description = description
	this.color = color
	timestamp = Clock.System.now()
	footer {
		text = "Bot mot relou"
	}
}

fun MessageCreateBuilder.embedServerError(user: Member) = embedErreur("Critique", user, "Erreur serveur", Color.error)
fun MessageModifyBuilder.embedServerError(user: Member) = embedErreur("Critique", user, "Erreur serveur", Color.error)