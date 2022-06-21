package fr.motrelou.bot

import dev.kord.cache.map.MapLikeCollection
import dev.kord.cache.map.internal.MapEntryCache
//import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.reply
import dev.kord.core.entity.Guild
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import fr.motrelou.bot.api.API
import fr.motrelou.bot.commands.*
//import io.ktor.client.*
//import io.ktor.client.engine.cio.*
//import io.ktor.client.request.*
//import io.ktor.http.*
import kotlinx.coroutines.flow.firstOrNull

lateinit var kord: Kord

suspend fun main() {
	kord = Kord(System.getenv("TOKEN")!!){
		enableShutdownHook = true
		cache {
			users { cache, description ->
				MapEntryCache(cache, description, MapLikeCollection.concurrentHashMap())
			}
		}
	}

	val guilds = mutableListOf<Guild>()
	kord.guilds.collect {
		guilds.add(it)
	}

	//val list = mutableMapOf<Snowflake, Snowflake>()
	val api = API()
	val add = Add(api)
	val get = Get(api)
	val random = Random(api)
	val getAll = GetAll(api)
	val getEmoji = GetEmoji()
	val addDef = AddDef(api)

	guilds.forEach {
		add.register(it.id)
		get.register(it.id)
		random.register(it.id)
		getAll.register(it.id)
		getEmoji.register(it.id)
		addDef.register(it.id)
	}

	kord.on<MessageCreateEvent> {
		message.mentionedUsers.firstOrNull { it.id == kord.selfId}?.let{
			message.reply {
				content = getKaamelottResponse()
			}
		}
	}

	/*Runtime.getRuntime().addShutdownHook(object : Thread(){
		override fun run() {
			runBlocking {
				val client = HttpClient(CIO)
				println("Removing command")
				list.forEach { (guild, command) ->
					client.request {
						url("https://discord.com/api/v10/application/${kord.selfId}/guilds/$guild/commands/$command")
						method = HttpMethod.Delete
						headers["Authorization"] = "Bot ${System.getenv("TOKEN")!!}"
					}
				}
				println("Shutting Down\nBye :)")
			}
		}
	})*/

	kord.login {
		@OptIn(PrivilegedIntent::class)
		intents += Intent.MessageContent
	}
}