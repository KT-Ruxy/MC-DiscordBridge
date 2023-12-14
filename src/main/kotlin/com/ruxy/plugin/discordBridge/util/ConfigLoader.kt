/*
Copyright ⒞ 2023 Ruxy

This project is open source.

This project is distributed under the MIT License.

All permissions for this project are under the MIT License.
*/

package com.ruxy.plugin.discordBridge.util

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import org.slf4j.Logger
import java.io.File

class ConfigLoader(plugin: JavaPlugin, private val logger: Logger) {

    private val pluginDataFolder = plugin.dataFolder

    var botToken: String? = null //Require
    var logChannelID: String? = null //Require
    private var enableConsoleBridge: Boolean? = null
    var consoleChannelID: String? = null
    var botOnlineStatus: String? = null
    var botActivityStatus: String? = null
    var botActivityMessage: String? = null
    var messageStyle: String? = null
    var webHookUrl: String? = null //Require

    fun loadConfig() {
        logger.info("loading config File...")
        val configFile = File(pluginDataFolder, "config.yml")
        val config = YamlConfiguration.loadConfiguration(configFile)

        botToken = config.getString("BotToken")
        logChannelID = config.getString("Log_Channel_ID")
        enableConsoleBridge = config.getBoolean("Enable_Console_Bridge")
        consoleChannelID = config.getString("Console_Channel_ID")
        botOnlineStatus = config.getString("Bot_Online_Status")
        botActivityStatus = config.getString("Bot_Activity_Status")
        botActivityMessage = config.getString("Bot_Activity_Message")
        messageStyle = config.getString("Message_Style")
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

    fun doBridgeConsole(): Boolean {
        return enableConsoleBridge == true && consoleChannelID != null
    }

}