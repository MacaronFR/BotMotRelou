package fr.motrelou.bot.commands

import dev.kord.core.behavior.interaction.response.respond
import fr.motrelou.bot.api.API
import fr.motrelou.bot.embeds.embedServerError
import fr.motrelou.bot.embeds.listEmbed
import fr.motrelou.bot.excpetions.APIException

class GetAll(private val api: API): RegisterCommand(Command(
	"getall",
	"Récupérer tout les mots",
	listOf(
		Parameter(ParameterType.Int, "page", "La page à récupérer")
	)
), {
	val page = (interaction.command.integers["page"] ?: 1) - 1
	interaction.deferPublicResponse().respond {
		try{
			listEmbed(api.mot(page), interaction.guildId, page)
		}catch (e: APIException){
			when(e.code){
				//prévoir pour pagination
				else -> embedServerError(interaction.user)
			}
		}
	}
})