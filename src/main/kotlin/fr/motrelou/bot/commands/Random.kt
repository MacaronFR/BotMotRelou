package fr.motrelou.bot.commands

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondPublic
import fr.motrelou.bot.api.API
import fr.motrelou.bot.embeds.embedMot

class Random(kord: Kord, private val api: API): RegisterCommand(kord,
	Command(
		"random",
		"Récupérer un mot aléatoire"
	),
	{
		interaction.respondPublic {
			api.random().let{ mot ->
				embedMot(mot, kord.getUser(Snowflake(mot.createur))!!.asMember(interaction.guildId))
			}
		}
	}
)