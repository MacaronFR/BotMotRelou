package fr.motrelou.bot.commands

import dev.kord.core.behavior.interaction.respondPublic
import fr.motrelou.bot.api.API
import fr.motrelou.bot.embeds.embedMot
import fr.motrelou.bot.embeds.embedServerError
import fr.motrelou.bot.excpetions.APIException

class Random(private val api: API): RegisterCommand(
	Command(
		"random",
		"Récupérer un mot aléatoire"
	),
	{
		interaction.respondPublic {
			try {
				embedMot(api.random(), interaction.guildId)
			}catch (e: APIException){
				embedServerError(interaction.user)
			}
		}
	}
)