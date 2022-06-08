package fr.motrelou.bot.commands

import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.create.embed
import fr.motrelou.bot.api.API

class Add(kord: Kord, val api: API) : RegisterCommand(kord, Command(
	"add",
	"Ajouter un mot",
	listOf(
		Parameter(ParameterType.String, "mot", "Mot à ajouter", true),
		Parameter(ParameterType.String, "definition", "Définition du mot", true)
	)
), {
	val test = api.motAjout(
		interaction.command.strings["mot"]!!,
		interaction.command.strings["definition"]!!,
		interaction.user.id.toString()
	)
	interaction.respondPublic {
		embed {
			author = EmbedBuilder.Author().apply {
				name = test.createur
			}
			title = test.mot
			test.definitions.forEach {
				field("${it.index} : ${it.definition}", true){
					"${it.createur} at ${it.creation}"
				}
			}
		}
	}
})