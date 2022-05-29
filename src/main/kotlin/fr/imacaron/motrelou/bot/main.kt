package fr.imacaron.motrelou.bot

import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import dev.kord.rest.builder.interaction.string
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

suspend fun main() {
	val kord = Kord(System.getenv("TOKEN")!!)
	val commandRegister = CommandRegister(kord);
	val client = HttpClient(CIO) {
		install(ContentNegotiation) {
			json()
		}
	}
	val baseUrl = System.getenv("BASE_URL") ?: "https://imacaron.fr/motrelou";

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
		val word = command.strings["mot"]!!
		val definition = command.strings["définition"]!!
		val author = interaction.user.id.value

		client.post("$baseUrl/mot") {
			contentType(ContentType.Application.Json)
			setBody(AddWord(word, definition, author.toString()))
		}

		response.respond {
			this.content = "Reçu ! $word"
		}
	}

	kord.login {
		@OptIn(PrivilegedIntent::class)
		intents += Intent.MessageContent
	}
}