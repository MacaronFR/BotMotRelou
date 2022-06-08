package fr.motrelou.bot.excpetions

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

class APIException(data: String): Throwable(){

	override val message: String
	val code: Int
	val data: String?

	init{
		Json.parseToJsonElement(data).jsonObject.let{
			message = it["message"]!!.toString()
			code = it["code"]!!.toString().toInt()
			this.data = it["data"]?.toString()
		}
	}
}