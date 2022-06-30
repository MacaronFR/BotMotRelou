package fr.motrelou.bot.embeds

import dev.kord.common.DiscordTimestampStyle
import dev.kord.common.entity.ButtonStyle
import dev.kord.rest.builder.component.ActionRowBuilder
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.create.MessageCreateBuilder
import dev.kord.rest.builder.message.create.actionRow
import dev.kord.rest.builder.message.create.embed
import dev.kord.rest.builder.message.modify.MessageModifyBuilder
import dev.kord.rest.builder.message.modify.actionRow
import dev.kord.rest.builder.message.modify.embed
import fr.motrelou.bot.toMessageFormat
import java.time.LocalDateTime

private fun EmbedBuilder.voteEmbedBuilder(mot: String, pour: Int, contre: Int, end: LocalDateTime, votant: String, deleted: Boolean? = null){
	title = "Suppression du mot **$mot**"
	description = "Veuillez voter pour la suppression de **$mot** du dictionnaire"
	field {
		name = "Fin"
		value = end.toMessageFormat(DiscordTimestampStyle.RelativeTime)
	}
	field {
		name = "**POUR**"
		value = pour.toString()
		inline = true
	}
	field {
		name = "**CONTRE**"
		value = contre.toString()
		inline = true
	}
	field {
		name = "Votant"
		value = votant
		inline = false
	}
	deleted?.let{
		field{
			name = "Résultat"
			value = if(deleted){
				"Mot supprimé"
			}else{
				"Mot conservé"
			}
			inline = false
		}
	}
}

private fun ActionRowBuilder.voteButtonBuilder(mot: String, disabled: Boolean){
	interactionButton(ButtonStyle.Danger, "${mot}pour") {
		label = "POUR"
		this.disabled = disabled
	}
	interactionButton(ButtonStyle.Success, "${mot}contre") {
		label = "CONTRE"
		this.disabled = disabled
	}
}

fun MessageCreateBuilder.voteEmbed(mot: String, pour: Int, contre: Int, end: LocalDateTime, votant: String, deleted: Boolean? = null){
	embed{
		voteEmbedBuilder(mot, pour, contre, end, votant, deleted)
	}
}

fun MessageCreateBuilder.voteButton(mot: String){
	actionRow {
		voteButtonBuilder(mot, false)
	}
}

fun MessageModifyBuilder.voteEmbed(mot: String, pour: Int, contre: Int, end: LocalDateTime, votant: String, deleted: Boolean? = null){
	embed{
		voteEmbedBuilder(mot, pour, contre, end, votant, deleted)
	}
}

fun MessageModifyBuilder.voteButton(mot: String, disabled: Boolean = false){
	actionRow {
		voteButtonBuilder(mot, disabled)
	}
}
