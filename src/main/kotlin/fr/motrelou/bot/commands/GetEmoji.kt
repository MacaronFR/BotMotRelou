package fr.motrelou.bot.commands

import dev.kord.core.behavior.interaction.respondPublic

class GetEmoji: RegisterCommand(
	Command(
	"getemoji",
	"Récupérer l'image d'un émoji",
	listOf(
		Parameter(
			ParameterType.String,
			"emoji",
			"L'émoji à récupérer",
			true
	))
), {
	interaction.respondPublic {
		val (animated, prefix) = if(interaction.command.strings["emoji"]!!.startsWith("<a:")){
			true to "<a:"
		}else{
			false to "<:"
		}
		val emoji = interaction.command.strings["emoji"]!!.removePrefix(prefix).removeSuffix(">").split(":")
		if(emoji.size >= 2) {
			if(animated){
				content = "https://cdn.discordapp.com/emojis/a_${emoji[1]}.gif"
			}else {
				content = "https://cdn.discordapp.com/emojis/${emoji[1]}.png"
			}
		}else{
			content = "C'est un émoji unicode débilus"
		}
	}
})