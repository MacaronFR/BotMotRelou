package fr.motrelou.bot.commands

import dev.kord.common.Color
import dev.kord.core.behavior.interaction.respondPublic
import fr.motrelou.bot.api.API
import fr.motrelou.bot.embeds.embedErreur
import fr.motrelou.bot.embeds.embedMot
import fr.motrelou.bot.embeds.embedServerError
import fr.motrelou.bot.excpetions.APIException
import fr.motrelou.bot.warning

class Get(private val api: API): RegisterCommand(
	Command(
		"get",
		"Récupérer un mot précis",
		listOf(
			Parameter(ParameterType.String, "mot", "Le mot à récupérer", true)
		)
	), {
		try{
			val res = api.mot(interaction.command.strings["mot"]!!)
			interaction.respondPublic {
				embedMot(res, interaction.guildId)
			}
		}catch (e: APIException){
			interaction.respondPublic {
				val user = interaction.user.asMember(interaction.guildId)
				when(e.code){
					404 -> embedErreur(interaction.command.strings["mot"]!!, user, "Le mot est introuvable", Color.warning)
					else -> embedServerError(user)
				}
			}
		}
	}
)
