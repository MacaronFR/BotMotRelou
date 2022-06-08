package fr.motrelou.bot

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.entity.Member
import java.time.LocalDateTime

suspend fun Kord.getUserInGuild(id: Snowflake, guild: Snowflake): Member? {
	return getUser(id)?.asMember(guild)
}

fun LocalDateTime.formatFrench(): String{
	val day = if(this.dayOfMonth < 10){
		"0${this.dayOfMonth}"
	}else{
		this.dayOfMonth.toString()
	}
	val month = if(this.month.value < 10){
		"0${this.month.value}"
	}else{
		this.month.toString()
	}
	return "$day/$month/${this.year}"
}