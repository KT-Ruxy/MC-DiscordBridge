/*
Copyright ⒞ 2023 Ruxy

This project is open source.

This project is distributed under the MIT License.

All permissions for this project are under the MIT License.
*/

package com.inf_ruxy.plugin.discordBridge.bridge.eventHandlers

import com.inf_ruxy.plugin.discordBridge.DiscordBridgeApi.discordEmbedManager
import com.inf_ruxy.plugin.discordBridge.DiscordBridgeApi.config
import com.inf_ruxy.plugin.discordBridge.DiscordBridgeApi.discordManager
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

@Suppress("unused")
class Events : Listener {

    //region Minecraft Events
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        discordEmbedManager.sendPlayerJoinEmbed(event.player)
    }

    @EventHandler
    fun onPlayerLeft(event: PlayerQuitEvent) {
        discordEmbedManager.sendPlayerLeftEmbed(event.player)
    }

    @EventHandler
    fun onPlayerDeathEvent(event: PlayerDeathEvent) {
        discordEmbedManager.sendPlayerDeathEmbed(event.player, event.deathMessage)
    }

    @EventHandler
    fun onPlayerChat(event: AsyncPlayerChatEvent) {
        val messageStyle = config.messageStyle
        val player = event.player
        val message = event.message
        val mcId = player.name
        val imageUrl = "https://crafthead.net/cube/${player.uniqueId}"
        val webhookUrl = config.webHookUrl!!

        when (messageStyle) {

            "classic" -> {
                val formattedMessage = "<$mcId> » $message"
                discordManager.sendToDiscord(formattedMessage)
            }

            "modern" -> {
                discordManager.sendWebhookMessage(webhookUrl, message, mcId, imageUrl)
            }
            else -> {
                val formattedMessage = "<$mcId> » $message"
                discordManager.sendToDiscord(formattedMessage)
            }
        }
    }
    //endregion

    fun handleDiscordMessage(event: MessageReceivedEvent) {
        val channelId = config.logChannelID

        if (event.channel.id != channelId) return

        if (event.author.isBot) return

        val member = event.member
        val memberId = event.member?.user?.id ?: "00000000000000000000"
        val idSuggest = "<@$memberId>"
        val memberName = event.member?.user?.name ?: return
        val highestRole = member?.roles?.maxByOrNull { it.position }
        val roleName = highestRole?.name
        val roleId = highestRole?.id.toString()
        val roleIdSuggest = "<@&$roleId>"
        val roleColor = highestRole?.colorRaw ?: 0xFFFFFF
        val kyoriRoleColor = TextColor.color((roleColor shr 16) and 0xFF, (roleColor shr 8) and 0xFF, roleColor and 0xFF)

        val componentMessage = if (roleName != null) {
            Component.text("[", NamedTextColor.WHITE)
                .append(Component.text("Discord", NamedTextColor.AQUA))
                .append(Component.text(" | ", NamedTextColor.WHITE))
                .append(Component.text(roleName, kyoriRoleColor)
                    .clickEvent(ClickEvent.suggestCommand(roleIdSuggest)))
                .append(Component.text("]", NamedTextColor.WHITE))
                .append(Component.text(" "))
                .append(Component.text(memberName)
                    .clickEvent(ClickEvent.suggestCommand(idSuggest)))
                .append(Component.text(" » ${event.message.contentDisplay}"))
        } else {
            Component.text("[", NamedTextColor.WHITE)
                .append(Component.text("Discord", NamedTextColor.AQUA))
                .append(Component.text("] ", NamedTextColor.WHITE))
                .append(Component.text(memberName)
                    .clickEvent(ClickEvent.suggestCommand(idSuggest)))
                .append(Component.text(" » ${event.message.contentDisplay}"))
        }

        Bukkit.getServer().sendMessage(componentMessage)

    }

}