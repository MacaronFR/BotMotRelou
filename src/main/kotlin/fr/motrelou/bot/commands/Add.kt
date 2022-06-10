package fr.motrelou.bot.commands

import dev.kord.common.Color
import dev.kord.core.behavior.interaction.respondPublic
import fr.motrelou.bot.api.API
import fr.motrelou.bot.embeds.embedErreur
import fr.motrelou.bot.embeds.embedMot
import fr.motrelou.bot.embeds.embedServerError
import fr.motrelou.bot.excpetions.APIException
import fr.motrelou.bot.getUserInGuild
import fr.motrelou.bot.warning

class Add(private val api: API) : RegisterCommand(Command(
	"add",
	"Ajouter un mot",
	listOf(
		Parameter(ParameterType.String, "mot", "Mot à ajouter", true),
		Parameter(ParameterType.String, "definition", "Définition du mot", true)
	)
), {
	val user = kord.getUserInGuild(interaction.user.id, interaction.guildId)!!
	try {
		val result = api.motAjout(
			interaction.command.strings["mot"]!!,
			interaction.command.strings["definition"]!!,
			interaction.user.id.toString()
		)
		interaction.respondPublic {
			embedMot(result, interaction.guildId)
		}
	}catch(e: APIException){
		interaction.respondPublic {
			when(e.code){
				409 -> embedErreur("Mot existant", user, "Le mot **${interaction.command.strings["mot"]!!}** existe déjà dans la base", Color.warning)
				else -> embedServerError(user)
			}
		}
	}
})