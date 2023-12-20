/*
Copyright ⒞ 2023 Ruxy

This project is open source.

This project is distributed under the MIT License.

All permissions for this project are under the MIT License.
*/

package com.inf_ruxy.plugin.discordBridge.manager

import org.bukkit.plugin.java.JavaPlugin
import org.slf4j.Logger
import java.io.File

class ConfigManager(private val plugin: JavaPlugin, private val logger: Logger) {

    private val dataFolder = plugin.dataFolder

    fun checkDataFolder() {
        if(!dataFolder.exists()) {
            logger.info("Generating DataFolder...")
            dataFolder.mkdirs()

        } else {
            logger.info("DataFolder already generated.")
        }
        return

    }

    fun checkConfigFile() {
        val configFile = File(dataFolder, "config.yml")
        if (!configFile.exists()) {
            logger.info("Generating configFile...")
            configFile.parentFile.mkdirs()
            plugin.saveResource("config.yml", false)
        } else {
            logger.info("ConfigFile already generated.")
        }
        return

    }

}