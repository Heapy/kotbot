@file:JvmName("Application")

package io.heapy.tgpt

import io.heapy.tgpt.bot.createApplicationModule

suspend fun main() {
    createApplicationModule {}
        .start()
}
