package fr.motrelou.bot.commands

import dev.kord.common.Color
import dev.kord.common.entity.ButtonStyle
import dev.kord.core.behavior.edit
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.ButtonInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.message.modify.actionRow
import fr.motrelou.bot.api.API
import fr.motrelou.bot.embeds.embedErreur
import fr.motrelou.bot.embeds.embedServerError
import fr.motrelou.bot.embeds.listEmbed
import fr.motrelou.bot.excpetions.APIException
import fr.motrelou.bot.getNextPage
import fr.motrelou.bot.getPrefix
import fr.motrelou.bot.kord
import fr.motrelou.bot.warning

class Search(private val api: API): RegisterCommand(
	Command(
		"search",
		"Rechercher un mot",
		listOf(
			Parameter(ParameterType.String, "search", "Chaine à rechercher", true),
			Parameter(ParameterType.Int, "page", "Page à afficher")
		)
	), {
		interaction.deferPublicResponse().respond {
			try {
				val search = interaction.command.strings["search"]!!
				val page = interaction.command.integers["page"] ?: 0
				val mots = api.search(search, page)
				if(mots.isNotEmpty()) {
					listEmbed(mots, interaction.guildId, page, search)
				}else{
					embedErreur("Aucun mot trouver", interaction.user, "Aucun mot correspondant à **$search** n'a été trouvé", Color.warning)
				}
			} catch(e: APIException) {
				when(e.code){
					//prévoir
					else -> embedServerError(interaction.user)
				}
			}
		}
	}
){
	init {
		kord.on<ButtonInteractionCreateEvent>{
			if(interaction.message.data.interaction.value?.name == "search"){
				val resp = interaction.deferPublicResponse()
				val page = interaction.component.getNextPage()!!
				val search = interaction.component.customId.getPrefix()
				val mots = api.search(search, page)
				if(mots.isNotEmpty()) {
					interaction.message.edit {
						listEmbed(mots, interaction.message.getGuild().id, page, search)
					}
				} else {
					interaction.message.edit {
						actionRow {
							interactionButton(ButtonStyle.Primary, "${search}prev${page - 1}") {
								label = "⬅️ Prev"
							}
						}
					}
				}
				resp.delete()
			}
		}
	}
}