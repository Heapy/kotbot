@file:JvmName("Playground")
package io.heapy.kotbot.bot

import io.heapy.komok.tech.config.dotenv.dotenv
import io.heapy.kotbot.bot.method.DeleteMessage
import io.heapy.kotbot.bot.method.GetMe
import io.heapy.kotbot.bot.model.LongChatId

suspend fun main() {
    val dotenv = dotenv()
    val env = dotenv.properties

    val kotbot = Kotbot(
        token = env.getValue("KOTBOT_TOKEN"),
    )

    val me = kotbot.execute(GetMe())

    println(me)

    kotbot.receiveUpdates()
        .collect {
            println("Update $it")
            try {
                kotbot.execute(DeleteMessage(
                    chat_id = LongChatId(it.message?.chat?.id!!),
                    message_id = it.message.message_id
                ))
            } catch (e: Exception) {
                println(e.message)
            }
        }
}

