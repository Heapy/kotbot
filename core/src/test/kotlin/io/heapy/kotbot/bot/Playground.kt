@file:JvmName("Playground")
package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.method.DeleteMessage
import io.heapy.kotbot.bot.method.GetMe
import io.heapy.kotbot.bot.model.LongChatId
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

suspend fun main() {
    val dotenv = dotenv()

    val kotbot = Kotbot(
        token = dotenv.getValue("KOTBOT_TOKEN"),
    )

    val me = kotbot.execute(GetMe())

    println(me)

    kotbot.receiveUpdates()
        .onEach {
            println("Update $it")
            try {
                kotbot.execute(DeleteMessage(
                    chat_id = LongChatId(it.message?.chat?.id!!),
                    message_id = it.message?.message_id!!
                ))
            } catch (e: Exception) {
                println(e.message)
            }
        }
        .collect()
}

