package com.ruxy.plugin.discordBridge

import com.ruxy.plugin.discordBridge.discord.DiscordManager
import com.ruxy.plugin.discordBridge.manager.ConfigManager
import com.ruxy.plugin.discordBridge.util.ConfigLoader
import com.ruxy.plugin.discordBridge.util.InitMessage
import org.bukkit.plugin.java.JavaPlugin
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object DiscordBridgeApi {

    private val logger: Logger = LoggerFactory.getLogger("DiscordBridge")

    private lateinit var plugin: JavaPlugin
    private lateinit var configManager: ConfigManager
    private lateinit var configLoader: ConfigLoader
    private lateinit var initMessage: InitMessage
    private lateinit var discordManager: DiscordManager

    fun load(plugin: JavaPlugin) {
        this.plugin = plugin
        configManager = ConfigManager(plugin, logger)
        configLoader = ConfigLoader(plugin, logger)

        configManager.checkDataFolder()
        configManager.checkConfigFile()
        configLoader.loadConfig()
    }

    fun start() {
        val nullFields = configLoader.nullCheck()
        if (nullFields.isEmpty()) {
            initMessage = InitMessage(logger)
            initMessage.message()
            discordManager = DiscordManager(plugin, logger)
            discordManager.startBot()
        } else {
            logger.error("The following settings are null: ${nullFields.joinToString(", ")}")
        }
    }

    fun stop() {
        discordManager.stopBot()
    }
}