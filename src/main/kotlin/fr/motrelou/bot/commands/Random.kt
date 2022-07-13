package fr.motrelou.bot.commands

import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.behavior.interaction.updatePublicMessage
import dev.kord.core.event.interaction.ButtonInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.message.create.actionRow
import fr.motrelou.bot.api.API
import fr.motrelou.bot.embeds.embedMot
import fr.motrelou.bot.embeds.embedServerError
import fr.motrelou.bot.embeds.refreshButton
import fr.motrelou.bot.excpetions.APIException
import fr.motrelou.bot.kord

class Random(private val api: API): RegisterCommand(
	Command(
		"random",
		"Récupérer un mot aléatoire"
	),
	{
		interaction.respondPublic {
			try {
				embedMot(api.random(), interaction.guildId)
				actionRow {
					refreshButton()
				}
			}catch (e: APIException){
				embedServerError(interaction.user)
			}
		}
	}
){
	init {
		kord.on<ButtonInteractionCreateEvent> {
			if(interaction.component.customId == "random") {
				interaction.updatePublicMessage {
					embedMot(api.random(), interaction.message.getGuild().id)
					actionRow {
						refreshButton()
					}
				}
			}
		}
	}
}