package fr.motrelou.bot.commands

import dev.kord.common.Color
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondPublic
import fr.motrelou.bot.api.API
import fr.motrelou.bot.embeds.embedErreur
import fr.motrelou.bot.embeds.embedMot
import fr.motrelou.bot.embeds.embedServerError
import fr.motrelou.bot.excpetions.APIException

class Get(kord: Kord, private val api: API): RegisterCommand(kord,
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
				embedMot(res, kord.getUser(Snowflake(res.createur))!!.asMember(interaction.guildId))
			}
		}catch (e: APIException){
			interaction.respondPublic {
				val user = interaction.user.asMember(interaction.guildId)
				when(e.code){
					404 -> embedErreur(interaction.command.strings["mot"]!!, user, "Le mot est introuvable", Color(255, 127, 0))
					else -> embedServerError(user)
				}
			}
		}
	}
)