package fr.motrelou.bot.api

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class API {
	companion object{
		val ktor = HttpClient(CIO)
		val domain = System.getenv("BASE_URL")
	}

	suspend fun mot(): String = request("/mot")

	suspend fun random(): String = request("/mot/random")

	suspend fun motAjout(mot: String, def: String, createur: String): MotApi{
		val data = AddMot(mot, createur, def)
		val res = request("/mot", Json.encodeToString(data), HttpMethod.Post)
		println(res)
		return Json.decodeFromString(res)
	}

	private suspend fun request(path: String, method: HttpMethod = HttpMethod.Get): String{
		if(path[0] != '/'){
			path.padStart(1, '/')
		}
		println(path)
		val resp = ktor.request("$domain$path"){
			this.method = method
		}
		return resp.bodyAsText()
	}

	private suspend fun request(path: String, body: String, method: HttpMethod): String{
		if(path[0] != '/'){
			path.padStart(1, '/')
		}
		val resp = ktor.request("$domain$path"){
			this.method = method
			setBody(body)
			headers{
				append(HttpHeaders.ContentType, ContentType.Application.Json)
			}
		}
		return resp.bodyAsText()
	}
}