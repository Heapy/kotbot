package io.heapy.kotbot.bot.challenge

import kotlin.random.Random

class ChallengeGenerator(
    private val templates: List<ChallengeTemplate>,
) {
    init {
        require(templates.isNotEmpty()) { "At least one challenge template is required" }
    }

    fun generate(): Challenge {
        val seed = Random.nextLong()
        return generate(seed)
    }

    fun generate(seed: Long): Challenge {
        val random = Random(seed)
        val template = templates[random.nextInt(templates.size)]
        return template.generate(random).copy(seed = seed)
    }
}
