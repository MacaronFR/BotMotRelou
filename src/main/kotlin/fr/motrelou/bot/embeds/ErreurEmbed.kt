package fr.motrelou.bot.embeds

import dev.kord.common.Color
import dev.kord.core.entity.Member
import dev.kord.rest.builder.message.create.MessageCreateBuilder
import dev.kord.rest.builder.message.create.embed
import kotlinx.datetime.Clock

fun MessageCreateBuilder.embedErreur(message: String, user: Member, description: String, color: Color){
	embed {
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
}

fun MessageCreateBuilder.embedServerError(user: Member) = embedErreur("Critique", user, "Erreur serveur", Color(255, 0, 0))