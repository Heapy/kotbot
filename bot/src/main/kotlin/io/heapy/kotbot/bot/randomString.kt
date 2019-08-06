package io.heapy.kotbot.bot

import kotlin.random.Random

private val charPool = ('A'..'Z') + ('a'..'z') + ('0'..'9')

fun randomString(length: Int, random: Random = Random): String =
    (1..length)
        .map { random.nextInt(charPool.size) }
        .map { charPool[it] }
        .joinToString("")
