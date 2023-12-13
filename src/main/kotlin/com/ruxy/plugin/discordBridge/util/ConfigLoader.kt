package com.ruxy.plugin.discordBridge.util

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import org.slf4j.Logger
import java.io.File

class ConfigLoader(plugin: JavaPlugin, private val logger: Logger) {

    private val pluginDataFolder = plugin.dataFolder

    var botToken: String? = null //Require
    var logChannelID: String? = null //Require
    var botOnlineStatus: String? = null
    var botActivityStatus: String? = null
    var messageStyle: String? = null
    var webHookUrl: String? = null //Require

    fun loadConfig() {
        logger.info("loading config File...")
        val configFile = File(pluginDataFolder, "config.yml")
        val config = YamlConfiguration.loadConfiguration(configFile)

        botToken = config.getString("BotToken")
        logChannelID = config.getString("Log_Channel_ID")
        botOnlineStatus = config.getString("Bot_Online_Status")
        botActivityStatus = config.getString("Bot_Activity_Status")
        messageStyle = config.getString("Reports_Message_Style")
        webHookUrl = config.getString("WebHook_URL")

        logger.info("Loaded ConfigFile.")
        return
    }

    fun nullCheck(): List<String> {
        val nullFields = mutableListOf<String>()

        if (webHookUrl == null) {
            nullFields.add("webHookUrl")
        }
        if (botToken == null) {
            nullFields.add("botToken")
        }
        if (logChannelID == null) {
            nullFields.add("logChannelID")
        }
        return nullFields
    }

}