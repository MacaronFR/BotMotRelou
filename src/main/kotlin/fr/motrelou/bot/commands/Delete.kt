package fr.motrelou.bot.commands

import dev.kord.common.Color
import dev.kord.common.entity.ButtonStyle
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.edit
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.behavior.interaction.updatePublicMessage
import dev.kord.core.event.interaction.ButtonInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.message.create.actionRow
import dev.kord.rest.builder.message.create.embed
import fr.motrelou.bot.api.API
import fr.motrelou.bot.embeds.embedErreur
import fr.motrelou.bot.embeds.embedServerError
import fr.motrelou.bot.embeds.voteButton
import fr.motrelou.bot.embeds.voteEmbed
import fr.motrelou.bot.excpetions.APIException
import fr.motrelou.bot.kord
import fr.motrelou.bot.warning
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime

@OptIn(DelicateCoroutinesApi::class)
class Delete(val api: API): RegisterCommand(
	Command("delete", "Supprimer un mot", listOf(Parameter(ParameterType.String, "mot", "Le mot à supprimer", true))),
	{
		val mot = interaction.command.strings["mot"]!!
		try{
			api.mot(mot)
			val end = Instant.fromEpochMilliseconds(System.currentTimeMillis()).toLocalDateTime(TimeZone.UTC).toJavaLocalDateTime().plusHours(10)
			interaction.respondPublic {
				voteEmbed(mot, 0, 0, end, "Aucun")
				voteButton(mot)
			}
			GlobalScope.launch {
				delay(43200000L)
				val message = interaction.getOriginalInteractionResponseOrNull() ?: run{
					kord.getUser(Snowflake(259353995754078209))!!.getDmChannel().createMessage("Erreur lors de la récupération des résultats du vote pour supprimer le mot $mot dans la guilde ${interaction.getGuild().name} par l'utilisateur ${interaction.user.displayName}")
					return@launch
				}
				val pour = message.embeds[0].fields[1].value.toInt()
				val contre = message.embeds[0].fields[2].value.toInt()
				val votant = message.embeds[0].fields[3].value
				message.edit {
					voteEmbed(mot, pour, contre, end, votant)
					voteButton(mot, true)
				}
				kord.getUser(Snowflake(259353995754078209))!!.getDmChannel().createMessage {
					embed {
						title = "Vote pour suppression de **$mot**"
						description = "Résultat du vote pour la suppression du mot **$mot** du dictionnaire"
						field {
							name = "**POUR**"
							value = message.embeds[0].fields[1].value
							inline = true
						}
						field {
							name = "**CONTRE**"
							value = message.embeds[0].fields[2].value
							inline = true
						}
						field {
							name = "Vainqueur"
							value = message.embeds[0].fields.run {
								if(pour > contre) {
									"**Pour**"
								} else if(pour < contre) {
									"**Contre**"
								} else {
									"**Égalité**"
								}
							}
						}
					}
					actionRow {
						interactionButton(ButtonStyle.Danger, "delete$mot"){
							label = "Supprimer"
						}
						interactionButton(ButtonStyle.Secondary, "cancel"){
							label = "Annuler"
						}
					}
				}
				kord.on<ButtonInteractionCreateEvent> {
					if(interaction.component.customId == "cancel"){
						interaction.message.delete()
						message.edit {
							voteEmbed(mot, pour, contre, end, votant, false)
						}
					}else if(interaction.component.customId!!.startsWith("delete")){
						api.delete(interaction.component.customId!!.removePrefix("delete"))
						interaction.message.delete()
						message.edit {
							voteEmbed(mot, pour, contre, end, votant, true)
						}
					}
				}
			}
		}catch(e: APIException){
			when(e.code){
				404 -> interaction.respondPublic { embedErreur("Aucun mot à supprimer", interaction.user, "Le mot $mot n'a pas été trouvé", Color.warning) }
				else -> interaction.respondPublic { embedServerError(interaction.user) }
			}
		}
	}
) {
	init {
		kord.on<ButtonInteractionCreateEvent> {
			if(interaction.message.data.interaction.value?.name == "delete"){
				var pour = interaction.message.embeds[0].fields[1].value.toInt()
				var contre = interaction.message.embeds[0].fields[2].value.toInt()
				var votant = interaction.message.embeds[0].fields[3].value
				if(interaction.user.id.toString() in votant){
					interaction.respondEphemeral {
						content = "Vous avez déjà voté pour ce mot"
					}
					return@on
				}
				val end = Instant.fromEpochSeconds(interaction.message.embeds[0].fields[0].value.run {
					substring(indexOf(":") + 1, indexOf(":", indexOf(":") + 1))
				}.toLong()).toLocalDateTime(TimeZone.UTC)
				val mot = if(interaction.component.customId!!.contains("pour")){
					pour++
					interaction.component.customId!!.removeSuffix("pour")
				} else if(interaction.component.customId!!.contains("contre")){
					contre++
					interaction.component.customId!!.removeSuffix("contre")
				}else{
					""
				}
				votant = if(votant == "Aucun"){
					"<@${interaction.user.id}>"
				}else{
					"${votant}\n<@${interaction.user.id}>"
				}
				interaction.updatePublicMessage {
					voteEmbed(mot, pour, contre, end.toJavaLocalDateTime(), votant)
					voteButton(mot)
				}
			}
		}
	}
}