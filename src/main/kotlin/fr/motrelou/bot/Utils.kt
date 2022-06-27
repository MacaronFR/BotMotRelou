package fr.motrelou.bot

import dev.kord.common.Color
import dev.kord.common.DiscordTimestampStyle
import dev.kord.common.entity.Snowflake
import dev.kord.common.toMessageFormat
import dev.kord.core.Kord
import dev.kord.core.entity.Member
import dev.kord.core.entity.component.ButtonComponent
import kotlinx.datetime.toKotlinInstant
import java.time.LocalDateTime
import java.time.ZoneOffset

suspend fun Kord.getUserInGuild(id: Snowflake, guild: Snowflake): Member? = getUser(id)?.asMemberOrNull(guild)

val Color.Companion.success: Color get() = Color(88, 214, 141)
val Color.Companion.warning: Color get() = Color(233, 109, 20)
val Color.Companion.error: Color get() = Color(219, 23, 2)

fun LocalDateTime.toMessageFormat(style: DiscordTimestampStyle) = toInstant(ZoneOffset.UTC).toKotlinInstant().toMessageFormat(style)

fun ButtonComponent.getNextPage(): Long? =
	if("next" in customId!!) {
		customId!!.substring(customId!!.indexOf("next") + 4).toLong() + 1
	} else if("prev" in customId!!) {
		customId!!.substring(customId!!.indexOf("prev") + 4).toLong() - 1
	}else {
		null
	}

fun String?.getPrefix(): String =
	this?.run {
		if("next" in this) {
			removeSuffix(substring(indexOf("next")))
		} else if("prev" in this) {
			removeSuffix(substring(indexOf("prev")))
		}else{
			""
		}
	} ?: ""

fun ButtonComponent.getNextPageAndPrefix(): Pair<Long?, String> =
	if("next" in customId!!) {
		customId!!.substring(customId!!.indexOf("next") + 4)
			.toLong() + 1 to customId!!.removeSuffix(customId!!.substring(customId!!.indexOf("next")))
	} else if("prev" in customId!!) {
		customId!!.substring(customId!!.indexOf("prev") + 4)
			.toLong() - 1 to customId!!.removeSuffix(customId!!.substring(customId!!.indexOf("prev")))
	} else {
		null to ""
	}