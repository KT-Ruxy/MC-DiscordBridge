/*
Copyright ⒞ 2023 Ruxy

This project is open source.

This project is distributed under the MIT License.

All permissions for this project are under the MIT License.
*/

package com.inf_ruxy.plugin.discordBridge.bridge

import club.minnced.discord.webhook.WebhookClient
import club.minnced.discord.webhook.send.WebhookMessageBuilder
import com.inf_ruxy.plugin.discordBridge.DiscordBridgeApi.events
import com.inf_ruxy.plugin.discordBridge.DiscordBridgeApi.logger

import com.inf_ruxy.plugin.discordBridge.DiscordBridgeApi.config
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import java.util.logging.Handler
import java.util.logging.LogRecord
import javax.security.auth.login.LoginException

open class DiscordManager {

    var jda: JDA? = null

    //region DiscordBotStart/Stop
    fun startBot() {
        val token = config.botToken

        val onlineStatus = when (config.botOnlineStatus?.uppercase(Locale.getDefault())) {
            "DO_NOT_DISTURB" -> OnlineStatus.DO_NOT_DISTURB
            "INVISIBLE" -> OnlineStatus.INVISIBLE
            "IDLE" -> OnlineStatus.IDLE
            else -> OnlineStatus.ONLINE
        }
        val activityType = config.botActivityStatus ?: "playing"
        val activityMessage = config.botActivityMessage ?: "Minecraft"

        try {
            val activity = when (activityType.lowercase(Locale.getDefault())) {
                "playing" -> Activity.playing(activityMessage)
                "watching" -> Activity.watching(activityMessage)
                "listening" -> Activity.listening(activityMessage)
                "competing" -> Activity.competing(activityMessage)
                else -> Activity.playing(activityMessage)
            }

            jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.DIRECT_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .setStatus(onlineStatus)
                .setActivity(activity)
                .addEventListeners(discordListener)
                .build()
                .awaitReady()

            logger.info("The Discord Bot has been successfully activated.")
            sendToDiscord(":white_check_mark: **Server has Started!**")
        } catch (e: LoginException) {
            logger.error("Can't Start DiscordBot: ${e.message}")
        }
    }

    fun stopBot() {
        try {
            logger.info("DiscordBot has been successfully terminated.")
            sendToDiscord(":octagonal_sign: **Server has Stopped!**")
            jda?.shutdown()
        } catch (e: Exception) {
            logger.error("An error occurred during DiscordBot shutdown: ${e.message}")
        }
    }
    //endregion

    fun sendToDiscord(message: String) {
        val channelId = config.logChannelID!!
        val channel = channelId.let { jda?.getTextChannelById(it) }
        channel!!.sendMessage(message).queue()
    }

    fun sendConsoleMessage(message: String) {
        val cChannelId = config.consoleChannelID!!
        val channel = cChannelId.let { jda?.getTextChannelById(it) }
        channel?.sendMessage(message)?.queue()
    }

    fun handleConsole(plugin: JavaPlugin) {
        plugin.server.logger.addHandler(object : Handler() {
            override fun publish(record: LogRecord) {
                sendConsoleMessage(record.message)
            }
            override fun flush() {}
            override fun close() {}
        })
    }

    private val discordListener = object : ListenerAdapter() {
        override fun onMessageReceived(event: MessageReceivedEvent) {
            events.handleDiscordMessage(event)
        }
    }

    fun sendWebhookMessage(webhookUrl: String, message: String, username: String, avatarUrl: String) {
        WebhookClient.withUrl(webhookUrl).use { client ->
            val builder = WebhookMessageBuilder()
            builder.setUsername(username)
            builder.setAvatarUrl(avatarUrl)
            builder.setContent(message)

            client.send(builder.build()).thenAccept {
                logger.info("Message sent successfully.")
            }.exceptionally { throwable ->
                logger.error("Error sending message: ${throwable.message}")
                null
            }
        }
    }


}