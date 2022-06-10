package fr.motrelou.bot

import dev.kord.common.Color
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.entity.Member
import java.time.LocalDateTime

suspend fun Kord.getUserInGuild(id: Snowflake, guild: Snowflake): Member? = getUser(id)?.asMemberOrNull(guild)

fun LocalDateTime.formatFrench(): String{
	val day = if(this.dayOfMonth < 10){
		"0${this.dayOfMonth}"
	}else{
		this.dayOfMonth.toString()
	}
	val month = if(this.month.value < 10){
		"0${this.month.value}"
	}else{
		this.month.value.toString()
	}
	return "$day/$month/${this.year}"
}

val Color.Companion.success: Color get() = Color(88, 214, 141)
val Color.Companion.warning: Color get() = Color(233, 109, 20)
val Color.Companion.error: Color get() = Color(219, 23, 2)