package com.ruxy.plugin.discordBridge.discord

import com.ruxy.plugin.discordBridge.util.ConfigLoader
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import org.bukkit.plugin.java.JavaPlugin
import org.slf4j.Logger
import java.util.*
import javax.security.auth.login.LoginException

class DiscordManager(plugin: JavaPlugin, private val logger: Logger) {

    private val config = ConfigLoader(plugin, logger)

    private var jda: JDA? = null

    //region DiscordBotStart/Stop
    fun startBot() {
        val token = config.botToken

        val onlineStatus = when (config.botOnlineStatus?.uppercase(Locale.getDefault())) {
            "DO_NOT_DISTURB" -> OnlineStatus.DO_NOT_DISTURB
            "INVISIBLE" -> OnlineStatus.INVISIBLE
            "IDLE" -> OnlineStatus.IDLE
            else -> OnlineStatus.ONLINE
        }
        val activityStatus = config.botActivityStatus ?: "Minecraft"

        try {
            jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.DIRECT_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .setStatus(onlineStatus)
                .setActivity(Activity.playing(activityStatus))
                .build()

            logger.info("The Discord Bot has been successfully activated.")
            sendToDiscord(":white_check_mark: **Server has Started!**")
        } catch (e: LoginException) {
            logger.error("Can't Start DiscordBot: ${e.message}")
        }
    }

    fun stopBot() {
        try {
            jda?.shutdown()
            logger.info("DiscordBot has been successfully terminated.")
            sendToDiscord(":octagonal_sign: **Server has Stopped!**")
        } catch (e: Exception) {
            logger.error("An error occurred during DiscordBot shutdown: ${e.message}")
        }
    }
    //endregion

    private fun sendToDiscord(message: String) {
        val channelId = config.logChannelID
        val channel = channelId?.let { jda?.getTextChannelById(it) }
        channel?.sendMessage(message)?.queue()
    }

}