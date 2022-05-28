package fr.imacaron.motrelou.bot

import dev.kord.core.Kord
import dev.kord.core.entity.ReactionEmoji
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent

suspend fun main() {
	val kord = Kord(System.getenv("TOKEN")!!)
	val pingPong = ReactionEmoji.Unicode("\uD83C\uDFD3")

	kord.on<MessageCreateEvent> {
		if (message.content != "!ping") return@on

		val response = message.channel.createMessage("Pong!")
		response.addReaction(pingPong)

		message.delete()
		response.delete()
	}

	kord.login {
		@OptIn(PrivilegedIntent::class)
		intents += Intent.MessageContent
	}
}