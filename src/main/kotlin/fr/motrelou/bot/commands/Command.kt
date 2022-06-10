package fr.motrelou.bot.commands

import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.application.GuildChatInputCommand
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.interaction.ChatInputCreateBuilder
import dev.kord.rest.builder.interaction.int
import dev.kord.rest.builder.interaction.string
import fr.motrelou.bot.kord

enum class ParameterType{
	String,
	Int;
}

data class Parameter(
	val type: ParameterType,
	val name: String,
	val desc: String,
	val required: Boolean? = null
)

data class Command(
	val name: String,
	val description: String,
	val param: List<Parameter> = listOf()
)

open class RegisterCommand(private val command: Command, private val handler: suspend GuildChatInputCommandInteractionCreateEvent.() -> Unit) {

	private val guildCommand: MutableMap<Snowflake, GuildChatInputCommand> = mutableMapOf()

	private fun ChatInputCreateBuilder.stringParam(name: String, desc: String, required: Boolean? = null){
		string(name, desc){
			this.required = required
		}
	}

	private fun ChatInputCreateBuilder.intParam(name: String, desc: String, required: Boolean? = null){
		int(name, desc){
			this.required = required
		}
	}

	suspend fun register(guild: Snowflake): Snowflake{
		guildCommand[guild] = kord.createGuildChatInputCommand(guild, command.name, command.description){
			command.param.forEach {
				when(it.type){
					ParameterType.String -> stringParam(it.name, it.desc, it.required)
					ParameterType.Int -> intParam(it.name, it.desc, it.required)
				}
			}
		}
		kord.on<GuildChatInputCommandInteractionCreateEvent>{
			if(interaction.command.rootId == guildCommand[guild]!!.id){
				handler(this)
			}
		}
		return guildCommand[guild]!!.id
	}
}