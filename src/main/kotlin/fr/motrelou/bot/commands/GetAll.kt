package fr.motrelou.bot.commands

import dev.kord.common.entity.ButtonStyle
import dev.kord.core.behavior.edit
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.ButtonInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.message.modify.actionRow
import fr.motrelou.bot.api.API
import fr.motrelou.bot.embeds.embedServerError
import fr.motrelou.bot.embeds.listEmbed
import fr.motrelou.bot.excpetions.APIException
import fr.motrelou.bot.kord

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
}){
	init {
		kord.on<ButtonInteractionCreateEvent> {
			val resp = interaction.deferPublicResponse()
			val page = if (interaction.component.customId!!.startsWith("next")) {
				interaction.component.customId!!.substring(4).toLong() + 1
			} else if(interaction.component.customId!!.startsWith("prev")){
				interaction.component.customId!!.substring(4).toLong() - 1
			}else{
				return@on
			}
			val mots = api.mot(page)
			if (mots.isNotEmpty()) {
				interaction.message.edit {
					listEmbed(mots, interaction.message.getGuild().id, page)
				}
			} else {
				interaction.message.edit {
					actionRow {
						interactionButton(ButtonStyle.Primary, "prev${page-1}") {
							label = "⬅️ Prev"
						}
					}
				}
			}
			resp.delete()
		}
	}
}