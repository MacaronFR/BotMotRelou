package fr.motrelou.bot.api

import fr.motrelou.bot.serializer.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class AddMot(
	val mot: String,
	val createur: String,
	val definition: String
)

@Serializable
data class MotApi(
	val mot: String,
	val createur: String,
	@Serializable(with = LocalDateTimeSerializer::class)
	val creation: LocalDateTime,
	val definitions: List<DefinitionApi>
)

@Serializable
data class DefinitionApi(
	val definition: String,
	val createur: String,
	@Serializable(with = LocalDateTimeSerializer::class)
	val creation: LocalDateTime,
	val index: Int
)