package fr.motrelou.bot.api

import fr.motrelou.bot.excpetions.APIException
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class API {
	companion object {
		val ktor = HttpClient(CIO)
		val domain: String = System.getenv("BASE_URL")
	}

	suspend fun mot(page: Long): List<MotApi> = request("/mot?page=$page")

	suspend fun mot(mot: String): MotApi = request("/mot/$mot")

	suspend fun random(): MotApi = request("/mot/random")

	suspend fun motAjout(mot: String, def: String, createur: String): MotApi =
		request("/mot", Json.encodeToString(AddMot(mot, createur, def)), HttpMethod.Post)

	suspend fun definitionAjout(mot: String, def: String, createur: String): MotApi = request("/mot/$mot/definition", Json.encodeToString(AddDefinition(def, createur)), HttpMethod.Post)

	suspend fun search(search: String, page: Long): List<MotApi> = request("/mot?recherche=$search&page=$page")

	private suspend inline fun <reified T> request(path: String, method: HttpMethod = HttpMethod.Get): T {
		val data = requestText(path, method)
		try {
			return Json.decodeFromString(data)
		} catch (_: SerializationException) {
			throw APIException(data)
		}
	}

	private suspend fun requestText(path: String, method: HttpMethod = HttpMethod.Get): String {
		if (path[0] != '/') {
			path.padStart(1, '/')
		}
		val resp = ktor.request("$domain$path") {
			this.method = method
		}
		return resp.bodyAsText()
	}

	private suspend inline fun <reified T> request(path: String, body: String, method: HttpMethod): T {
		val data = requestText(path, body, method)
		try {
			return Json.decodeFromString(data)
		} catch (_: SerializationException) {
			throw APIException(data)
		}
	}

	private suspend fun requestText(path: String, body: String, method: HttpMethod): String {
		if (path[0] != '/') {
			path.padStart(1, '/')
		}
		val resp = ktor.request("$domain$path") {
			this.method = method
			setBody(body)
			headers {
				append(HttpHeaders.ContentType, ContentType.Application.Json)
			}
		}
		return resp.bodyAsText()
	}
}