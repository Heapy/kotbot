package io.heapy.kotbot.bot

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.lang.System.getenv

suspend fun main() {
    val kotbot = Kotbot(
        token = getenv("KOTBOT_TOKEN"),
    )

    val me = kotbot.execute(GetMe())

    println(me)

    kotbot.receiveUpdates()
        .onEach {
            println("Update $it")
            try {
                kotbot.execute(DeleteMessage(
                    chatId = it.message?.chat?.id!!.toString(),
                    messageId = it.message?.message_id!!
                ))
            } catch (e: Exception) {
                println(e.message)
            }
        }
        .collect()
}

