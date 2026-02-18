package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.method.SetMyCommands
import io.heapy.kotbot.bot.model.BotCommand

// TODO: Use list of commands to generate BotCommandScope and BotCommand
// Run on startup
class SetBotCommands(
    private val kotbot: Kotbot,
) {
    suspend fun setBotCommands() {
        kotbot.executeSafely(
            SetMyCommands(
                commands = listOf(
                    BotCommand(
                        command = "start",
                        description = "Start the bot",
                    ),
                    BotCommand(
                        command = "help",
                        description = "Show help",
                    ),
                ),
            )
        )
    }
}
