package fr.imacaron.motrelou.bot

import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import dev.kord.rest.builder.interaction.string

suspend fun main() {
	val kord = Kord(System.getenv("TOKEN")!!)
	val commandRegister = CommandRegister(kord);

	commandRegister.register("add", "Ajoute un mot au dictionnaire.") {
		string("mot", "Le mot à ajouter.") {
			required = true
		}

		string("définition", "La première définition du mot.") {
			required = true
		}
	}.callback = {
		val response = this.interaction.deferPublicResponse()
		val command = interaction.command
		val word = command.strings["mot"]

		response.respond {
			this.content = "Reçu ! $word"
		}
	}

	kord.login {
		@OptIn(PrivilegedIntent::class)
		intents += Intent.MessageContent
	}
}