/*
Copyright ⒞ 2023 Ruxy

This project is open source.

This project is distributed under the MIT License.

All permissions for this project are under the MIT License.
*/

package com.ruxy.plugin.discordBridge.bridge.embed

import com.ruxy.plugin.discordBridge.DiscordBridgeApi.config
import com.ruxy.plugin.discordBridge.bridge.DiscordManager
import net.dv8tion.jda.api.EmbedBuilder
import org.bukkit.entity.Player
import java.awt.Color

class DiscordEmbedManager : DiscordManager() {

    fun sendPlayerJoinEmbed(player: Player) {
        val channel = this.jda?.getTextChannelById(config.logChannelID!!)
        val imageUrl = "https://crafthead.net/avatar/${player.uniqueId}"
        val embed = EmbedBuilder()
            .setColor(Color.GREEN)
            .setAuthor("${player.name} joined the server", null, imageUrl)
            .build()
        channel?.sendMessageEmbeds(embed)?.queue()
    }

    fun sendPlayerLeftEmbed(player: Player) {
        val channel = this.jda?.getTextChannelById(config.logChannelID!!)
        val imageUrl = "https://crafthead.net/avatar/${player.uniqueId}"
        val embed = EmbedBuilder()
            .setColor(Color.RED)
            .setAuthor("${player.name} left the server", null, imageUrl)
            .build()
        channel?.sendMessageEmbeds(embed)?.queue()
    }

    fun sendPlayerDeathEmbed(player: Player, deathMessage: String?) {
        val channel = this.jda?.getTextChannelById(config.logChannelID!!)
        val imageUrl = "https://crafthead.net/avatar/${player.uniqueId}"
        val embed = EmbedBuilder()
            .setColor(Color.BLACK)
            .setAuthor(deathMessage ?: "${player.name} died", null, imageUrl)
            .build()
        channel?.sendMessageEmbeds(embed)?.queue()
    }

}