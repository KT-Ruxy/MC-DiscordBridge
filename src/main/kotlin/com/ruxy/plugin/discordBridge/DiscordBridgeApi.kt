/*
Copyright ⒞ 2023 Ruxy

This project is open source.

This project is distributed under the MIT License.

All permissions for this project are under the MIT License.
*/

package com.ruxy.plugin.discordBridge

import com.ruxy.plugin.discordBridge.bridge.DiscordManager
import com.ruxy.plugin.discordBridge.bridge.embed.DiscordEmbedManager
import com.ruxy.plugin.discordBridge.bridge.eventHandlers.Events
import com.ruxy.plugin.discordBridge.manager.ConfigManager
import com.ruxy.plugin.discordBridge.util.ConfigLoader
import com.ruxy.plugin.discordBridge.util.InitMessage
import org.bukkit.plugin.java.JavaPlugin
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object DiscordBridgeApi {

    val logger: Logger = LoggerFactory.getLogger("DiscordBridge")

    private lateinit var plugin: JavaPlugin
    lateinit var discordEmbedManager: DiscordEmbedManager
    lateinit var events: Events
    lateinit var config: ConfigLoader
    private lateinit var configManager: ConfigManager
    private lateinit var initMessage: InitMessage
    lateinit var discordManager: DiscordManager

    fun load(plugin: JavaPlugin) {
        this.plugin = plugin
        configManager = ConfigManager(plugin, logger)
        config = ConfigLoader(plugin, logger)
        discordEmbedManager = DiscordEmbedManager()
        events = Events()

        configManager.checkDataFolder()
        configManager.checkConfigFile()
        config.loadConfig()
    }

    fun start() {
        val nullFields = config.nullCheck()
        if (nullFields.isEmpty()) {
            initMessage = InitMessage(logger)
            initMessage.message()
            discordManager = DiscordManager()
            discordManager.startBot()
            if (config.doBridgeConsole()) {
                discordManager.handleConsole(plugin)
            }
            plugin.server.pluginManager.registerEvents(events, plugin)
        } else {
            logger.error("The following settings are null: ${nullFields.joinToString(", ")}")
        }
    }

    fun stop() {
        discordManager.stopBot()
    }
}