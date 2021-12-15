package io.heapy.kotbot.bot

import kotlinx.coroutines.flow.collect
import java.lang.System.getenv

suspend fun main() {
    val kotbot = Kotbot(
        token = getenv("KOTBOT_TOKEN"),
    )

    val me = kotbot.execute(GetMe())

    kotbot.receiveUpdates()
        .collect {
            println("${me.first_name} received: ${it.message?.text}")
            // Belarus Kotlin User Group Bot received: Hi
        }
}

