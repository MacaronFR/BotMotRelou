package fr.motrelou.bot.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object LocalDateTimeSerializer: KSerializer<LocalDateTime> {
	override val descriptor: SerialDescriptor
		get() = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

	override fun deserialize(decoder: Decoder): LocalDateTime {
		val string = decoder.decodeString()
		return LocalDateTime.parse(string, DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss"))
	}

	override fun serialize(encoder: Encoder, value: LocalDateTime) {
		val string = value.format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss"))
		encoder.encodeString(string)
	}

}