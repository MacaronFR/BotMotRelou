package fr.motrelou.bot

import dev.kord.common.entity.ButtonStyle
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.edit
import dev.kord.core.entity.Guild
import dev.kord.core.event.interaction.ButtonInteractionCreateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import dev.kord.rest.builder.message.modify.actionRow
import fr.motrelou.bot.api.API
import fr.motrelou.bot.commands.*
import fr.motrelou.bot.embeds.listEmbed
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*

lateinit var kord: Kord

suspend fun main() {
	kord = Kord(System.getenv("TOKEN")!!)

	val guilds = mutableListOf<Guild>()
	kord.guilds.collect {
		guilds.add(it)
	}

	val list = mutableMapOf<Snowflake, Snowflake>()
	val api = API()
	val add = Add(api)
	val get = Get(api)
	val random = Random(api)
	val getAll = GetAll(api)

	guilds.forEach {
		add.register(it.id)
		get.register(it.id)
		random.register(it.id)
		getAll.register(it.id)
	}

	kord.on<ButtonInteractionCreateEvent> {
		val resp = interaction.deferPublicResponse()
		val page = if (interaction.component.customId!!.startsWith("next")) {
			interaction.component.customId!!.substring(4).toLong() + 1
		} else if(interaction.component.customId!!.startsWith("prev")){
			interaction.component.customId!!.substring(4).toLong() - 1
		}else{
			return@on
		}
		val mots = api.mot(page)
		if (mots.isNotEmpty()) {
			interaction.message.edit {
				listEmbed(mots, interaction.message.getGuild().id, page)
			}
		} else {
			interaction.message.edit {
				actionRow {
					interactionButton(ButtonStyle.Primary, "prev${page-1}") {
						label = "⬅️ Prev"
					}
				}
			}
		}
		resp.delete()
	}

	kord.login {
		@OptIn(PrivilegedIntent::class)
		intents += Intent.MessageContent
	}

	val client = HttpClient(CIO)
	list.forEach { (guild, command) ->
		client.request {
			url("https://discord.com/api/v10/application/${kord.selfId}/guilds/$guild/commands/$command")
			method = HttpMethod.Delete
			headers["Authorization"] = "Bot ${System.getenv("TOKEN")!!}"
		}
	}
}