package fr.motrelou.bot.excpetions

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

class APIException(data: String): Throwable(){

	override var message: String = ""
	private set
	var code: Int = 0
	private set
	var data: String? = null
	private set

	init{
		try {
			Json.parseToJsonElement(data).jsonObject.let {
				message = it["message"]!!.toString()
				code = it["code"]!!.toString().toInt()
				this.data = it["data"]?.toString()
			}
		}catch (_: SerializationException){
			message = "Server Error"
			code = 500
		}
	}
}