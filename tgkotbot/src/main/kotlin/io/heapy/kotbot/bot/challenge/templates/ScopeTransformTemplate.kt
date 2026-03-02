package io.heapy.kotbot.bot.challenge.templates

import io.heapy.kotbot.bot.challenge.Challenge
import io.heapy.kotbot.bot.challenge.ChallengeTemplate
import io.heapy.kotbot.bot.challenge.DistractorGenerator
import io.heapy.kotbot.bot.challenge.IntValue
import io.heapy.kotbot.bot.challenge.StringValue
import kotlin.random.Random

class ScopeTransformTemplate(
    private val distractorGenerator: DistractorGenerator,
) : ChallengeTemplate {
    override val key: String = "scope_transform"

    override fun generate(random: Random): Challenge {
        return when (random.nextInt(3)) {
            0 -> letChain(random)
            1 -> runTransform(random)
            else -> letRunCombo(random)
        }
    }

    private fun letChain(random: Random): Challenge {
        val initial = random.nextInt(1, 20)
        val add1 = random.nextInt(1, 10)
        val mul = random.nextInt(2, 5)

        val result = (initial + add1) * mul
        val correctValue = IntValue(result)

        val snippet = "$initial.let { it + $add1 }.let { it * $mul }"

        val distractors = distractorGenerator.generateDistractors(
            correctValue = correctValue,
            random = random,
        )

        val options = (distractors + correctValue.display()).shuffled(random)

        return Challenge(
            templateKey = key,
            seed = 0,
            snippet = snippet,
            correctAnswer = correctValue.display(),
            options = options,
        )
    }

    private fun runTransform(random: Random): Challenge {
        val word = listOf("kotlin", "java", "scala", "rust", "swift")[random.nextInt(5)]
        val n = random.nextInt(1, minOf(4, word.length))

        val result = word.run { drop(n).uppercase() }
        val correctValue = StringValue(result)

        val snippet = "\"$word\".run { drop($n).uppercase() }"

        val distractors = distractorGenerator.generateDistractors(
            correctValue = correctValue,
            random = random,
        )

        val options = (distractors + correctValue.display()).shuffled(random)

        return Challenge(
            templateKey = key,
            seed = 0,
            snippet = snippet,
            correctAnswer = correctValue.display(),
            options = options,
        )
    }

    private fun letRunCombo(random: Random): Challenge {
        val initial = random.nextInt(1, 15)
        val add = random.nextInt(1, 10)
        val sub = random.nextInt(1, 5)

        val result = (initial + add) - sub
        val correctValue = IntValue(result)

        val snippet = "$initial.let { it + $add }.run { this - $sub }"

        val distractors = distractorGenerator.generateDistractors(
            correctValue = correctValue,
            random = random,
        )

        val options = (distractors + correctValue.display()).shuffled(random)

        return Challenge(
            templateKey = key,
            seed = 0,
            snippet = snippet,
            correctAnswer = correctValue.display(),
            options = options,
        )
    }
}
