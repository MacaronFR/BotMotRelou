package fr.imacaron.motrelou.bot

import kotlinx.serialization.Serializable

@Serializable
data class AddWord(val mot: String, val createur: String, val definition: String)
