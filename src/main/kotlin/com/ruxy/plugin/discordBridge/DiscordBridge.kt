/*
Copyright ⒞ 2023 Ruxy

This project is open source.

This project is distributed under the MIT License.

All permissions for this project are under the MIT License.
*/

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