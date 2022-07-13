package fr.motrelou.bot.embeds

import dev.kord.common.Color
import dev.kord.common.DiscordTimestampStyle
import dev.kord.common.entity.ButtonStyle
import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.component.ActionRowBuilder
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.create.MessageCreateBuilder
import dev.kord.rest.builder.message.create.embed
import fr.motrelou.bot.*
import fr.motrelou.bot.api.MotApi
import kotlinx.datetime.Clock

suspend fun MessageCreateBuilder.embedMot(mot: MotApi, guild: Snowflake){
	embed {
		embedMot(mot, guild)
	}
}

suspend fun EmbedBuilder.embedMot(mot: MotApi, guild: Snowflake){
	val user = kord.getUser(Snowflake(mot.createur))!!
	val member = kord.getUserInGuild(user.id, guild)
	author {
		name = member?.displayName ?: user.username
		icon = member?.memberAvatar?.url ?: user.avatar?.url
	}
	title = "Mot : ${mot.mot}"
	description = "Le ${mot.creation.toMessageFormat(DiscordTimestampStyle.LongDate)} \n\n**Définitions :**"
	color = Color.success
	mot.definitions.forEach {
		field("${it.index} : ${it.definition}") {
			(kord.getUserInGuild(Snowflake(it.createur), guild)?.displayName ?: user.username ) + " le ${it.creation.toMessageFormat(DiscordTimestampStyle.LongDate)}"
		}
	}
	timestamp = Clock.System.now()
	footer {
		text = "Bot mot relou"
	}
}

fun ActionRowBuilder.refreshButton(){
	interactionButton(ButtonStyle.Primary, "random"){
		label = "Nouveau \uD83D\uDD04"
	}
}