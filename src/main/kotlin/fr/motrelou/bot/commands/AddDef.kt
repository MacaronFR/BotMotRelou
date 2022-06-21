package fr.motrelou.bot.commands

import dev.kord.common.Color
import dev.kord.core.behavior.interaction.respondPublic
import fr.motrelou.bot.api.API
import fr.motrelou.bot.embeds.embedErreur
import fr.motrelou.bot.embeds.embedMot
import fr.motrelou.bot.embeds.embedServerError
import fr.motrelou.bot.excpetions.APIException
import fr.motrelou.bot.warning

class AddDef(val api: API): RegisterCommand(
	Command(
		"adddef",
		"Ajouter une définition",
		listOf(
			Parameter(ParameterType.String, "mot", "Le mot auquel ajouter une définition", true),
			Parameter(ParameterType.String, "definition", "La définition à ajouter", true)
		)
	),
	{
		println("OLA")
		val mot = interaction.command.strings["mot"]!!
		val def = interaction.command.strings["definition"]!!
		interaction.respondPublic {
			try {
				api.definitionAjout(mot, def, interaction.user.id.toString()).let {
					embedMot(it, interaction.guildId)
				}
			}catch(e: APIException){
				val user = interaction.user.asMember(interaction.guildId)
				when(e.code){
					404 -> embedErreur("Mot **$mot** introuvable", user, "Le mot n'a pas été trouvé.\nAjoutez le avec la commande **/add**\n(Définition : *$def*)", Color.warning)
					else -> embedServerError(user)
				}
			}
		}
	}
)