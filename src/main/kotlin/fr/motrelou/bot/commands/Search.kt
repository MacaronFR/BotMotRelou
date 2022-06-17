package fr.motrelou.bot.commands

import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.ButtonInteractionCreateEvent
import dev.kord.core.on
import fr.motrelou.bot.api.API
import fr.motrelou.bot.embeds.listEmbed
import fr.motrelou.bot.excpetions.APIException
import fr.motrelou.bot.getNextPage
import fr.motrelou.bot.kord

class Search(private val api: API): RegisterCommand(
	Command(
		"search",
		"Rechercher un mot",
		listOf(
			Parameter(ParameterType.String, "search", "Chaine à rechercher", true),
			Parameter(ParameterType.Int, "page", "Page à afficher")
		)
	), {
		try{
			interaction.deferPublicResponse().respond {
				val (search, page) = Pair(interaction.command.strings["search"]!!, interaction.command.integers["page"] ?: 0)
				listEmbed(api.search(search, page), interaction.guildId, page)
			}
		}catch(e: APIException){
			println(e.code)
			interaction.respondPublic {
				content = "NIK"
			}
		}
	}
){
	init {
		kord.on<ButtonInteractionCreateEvent>{
			if(interaction.message.data.interaction.value?.name == "search"){
				interaction.deferPublicResponse().respond{
					val page = interaction.component.getNextPage()!!
//					val mots =
				}
			}
		}
	}
}