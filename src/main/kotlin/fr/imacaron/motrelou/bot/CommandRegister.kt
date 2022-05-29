package fr.imacaron.motrelou.bot

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.interaction.GlobalChatInputCreateBuilder

class CommandRegister(val kord: Kord) {
	class CommandCallback {
		var callback: (suspend GuildChatInputCommandInteractionCreateEvent.() -> Unit)? = null
	}

	private val callbacks: MutableMap<Snowflake, CommandCallback> = mutableMapOf()

	init {
		kord.on<GuildChatInputCommandInteractionCreateEvent> {
			val commandId = interaction.command.rootId
			callbacks[commandId]?.let {
				this@CommandRegister.callbacks[commandId]?.callback?.invoke(this)
			}
		}
	}

	suspend fun register(
		name: String,
		description: String,
		builder: GlobalChatInputCreateBuilder.() -> Unit = {},
	): CommandCallback {
		val command = kord.createGlobalChatInputCommand(name, description, builder)
		val callback = CommandCallback()
		callbacks[command.id] = callback
		return callback
	}
}