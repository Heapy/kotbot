package io.heapy.kotbot.bot.challenge.templates

import io.heapy.kotbot.bot.challenge.Challenge
import io.heapy.kotbot.bot.challenge.ChallengeTemplate
import io.heapy.kotbot.bot.challenge.DistractorGenerator
import io.heapy.kotbot.bot.challenge.IntValue
import io.heapy.kotbot.bot.challenge.StringValue
import kotlin.random.Random

class PairDestructuringTemplate(
    private val distractorGenerator: DistractorGenerator,
) : ChallengeTemplate {
    override val key: String = "pair_destructuring"

    override fun generate(random: Random): Challenge {
        return when (random.nextInt(3)) {
            0 -> pairSwap(random)
            1 -> tripleAccess(random)
            else -> pairArithmetic(random)
        }
    }

    private fun pairSwap(random: Random): Challenge {
        val a = random.nextInt(1, 20)
        val b = random.nextInt(1, 20)

        val result = "$b $a"
        val correctValue = StringValue(result)

        val snippet = buildString {
            appendLine("val (a, b) = $a to $b")
            append($$""""$b $a"""")
        }

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

    private fun tripleAccess(random: Random): Challenge {
        val a = random.nextInt(1, 10)
        val b = random.nextInt(10, 20)
        val c = random.nextInt(20, 30)

        val result = a + c
        val correctValue = IntValue(result)

        val snippet = buildString {
            appendLine("val (x, _, z) = Triple($a, $b, $c)")
            append("x + z")
        }

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

    private fun pairArithmetic(random: Random): Challenge {
        val a = random.nextInt(1, 15)
        val b = random.nextInt(1, 15)

        val result = a * b + a + b
        val correctValue = IntValue(result)

        val snippet = buildString {
            appendLine("val pair = $a to $b")
            appendLine("val (first, second) = pair")
            append("first * second + first + second")
        }

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
