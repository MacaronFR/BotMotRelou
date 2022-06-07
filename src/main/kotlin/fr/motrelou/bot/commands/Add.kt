package fr.motrelou.bot.commands

import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondPublic

class Add(kord: Kord): RegisterCommand(kord, Command(
	"add",
	"Ajouter un mot",
	listOf(
		Parameter(ParameterType.String, "mot", "Mot à ajouter", true),
		Parameter(ParameterType.String, "definition", "Définition du mot", true)
	)
), {
	println("ADD")
	interaction.respondPublic { content = "ADD" }
})