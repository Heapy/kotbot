package io.heapy.kotbot.bot.challenge

import kotlin.random.Random

data class Challenge(
    val templateKey: String,
    val seed: Long,
    val snippet: String,
    val correctAnswer: String,
    val options: List<String>,
)

interface ChallengeTemplate {
    val key: String
    fun generate(random: Random): Challenge
}
