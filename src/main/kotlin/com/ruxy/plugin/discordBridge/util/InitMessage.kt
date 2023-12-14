/*
Copyright ⒞ 2023 Ruxy

This project is open source.

This project is distributed under the MIT License.

All permissions for this project are under the MIT License.
*/

package com.ruxy.plugin.discordBridge.util

import org.slf4j.Logger

class InitMessage(private val logger: Logger) {

    fun message() {

        val space = "   "
        val message1 = "   \\033[32m \\033[1m Discord Bridge     "
        val message2 = " \\033[36m v1.0 Running\\033[0m on\\033[0m \\033[35m Bukkit "

        logger.info(space)
        logger.info(message1)
        logger.info(message2)
        logger.info(space)

    }

}