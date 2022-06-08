package fr.motrelou.bot.commands

import dev.kord.common.Color
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.rest.builder.message.create.embed
import fr.motrelou.bot.api.API
import fr.motrelou.bot.embeds.embedMot
import fr.motrelou.bot.excpetions.APIException
import fr.motrelou.bot.getUserInGuild

class Add(kord: Kord, private val api: API) : RegisterCommand(kord, Command(
	"add",
	"Ajouter un mot",
	listOf(
		Parameter(ParameterType.String, "mot", "Mot à ajouter", true),
		Parameter(ParameterType.String, "definition", "Définition du mot", true)
	)
), {
	try {
		val result = api.motAjout(
			interaction.command.strings["mot"]!!,
			interaction.command.strings["definition"]!!,
			interaction.user.id.toString()
		)
		interaction.respondPublic {
			embedMot(result, kord.getUserInGuild(Snowflake(result.createur), interaction.guildId)!!)
		}
	}catch(e: APIException){
		interaction.respondPublic {
			embed {
				author {
					name = "Bot Mot Relou"
				}
				title = "Erreur"
				description = when(e.code){
					409 -> "Mot existant"
					else -> "Erreur Serveur"
				}
				color = when(e.code){
					in 400..499 -> Color(255, 127, 0)
					else -> Color(255, 0, 0)
				}
			}
		}
	}
})