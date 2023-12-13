package com.ruxy.plugin.discordBridge

import org.bukkit.plugin.java.JavaPlugin

class DiscordBridge : JavaPlugin() {

    override fun onLoad() {
        DiscordBridgeApi.load(this)
    }

    override fun onEnable() {
        DiscordBridgeApi.start()
    }

    override fun onDisable() {
        DiscordBridgeApi.stop()
    }

}