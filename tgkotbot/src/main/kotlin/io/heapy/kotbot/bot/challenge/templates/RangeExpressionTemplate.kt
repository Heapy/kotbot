package io.heapy.kotbot.bot.challenge.templates

import io.heapy.kotbot.bot.challenge.Challenge
import io.heapy.kotbot.bot.challenge.ChallengeTemplate
import io.heapy.kotbot.bot.challenge.DistractorGenerator
import io.heapy.kotbot.bot.challenge.IntValue
import kotlin.random.Random

class RangeExpressionTemplate(
    private val distractorGenerator: DistractorGenerator,
) : ChallengeTemplate {
    override val key: String = "range_expression"

    override fun generate(random: Random): Challenge {
        return when (random.nextInt(3)) {
            0 -> filterSum(random)
            1 -> stepCount(random)
            else -> downToLast(random)
        }
    }

    private fun filterSum(random: Random): Challenge {
        val start = random.nextInt(1, 5)
        val end = random.nextInt(start + 5, start + 12)
        val modulo = random.nextInt(2, 4)

        val result = (start..end).filter { it % modulo == 0 }.sum()
        val correctValue = IntValue(result)

        val snippet = "($start..$end).filter { it % $modulo == 0 }.sum()"

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

    private fun stepCount(random: Random): Challenge {
        val start = random.nextInt(0, 5)
        val end = random.nextInt(start + 10, start + 25)
        val step = random.nextInt(2, 5)

        val result = (start..end step step).count()
        val correctValue = IntValue(result)

        val snippet = "($start..$end step $step).count()"

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

    private fun downToLast(random: Random): Challenge {
        val start = random.nextInt(8, 15)
        val end = random.nextInt(1, start - 3)
        val n = random.nextInt(1, 4)

        val result = (start downTo end).take(n).last()
        val correctValue = IntValue(result)

        val snippet = "($start downTo $end).take($n).last()"

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
