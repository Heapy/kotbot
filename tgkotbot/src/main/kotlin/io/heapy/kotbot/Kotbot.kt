@file:JvmName("Kotbot")

package io.heapy.kotbot

import io.heapy.kotbot.bot.createApplicationModule

suspend fun main() {
    createApplicationModule {}
        .start()
}
