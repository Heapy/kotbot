package io.heapy.kotbot.bot.challenge.templates

import io.heapy.kotbot.bot.challenge.Challenge
import io.heapy.kotbot.bot.challenge.ChallengeTemplate
import io.heapy.kotbot.bot.challenge.DistractorGenerator
import io.heapy.kotbot.bot.challenge.IntValue
import io.heapy.kotbot.bot.challenge.NullValue
import kotlin.random.Random

class NullSafetyTemplate(
    private val distractorGenerator: DistractorGenerator,
) : ChallengeTemplate {
    override val key: String = "null_safety"

    override fun generate(random: Random): Challenge {
        return when (random.nextInt(3)) {
            0 -> safeCallElvis(random)
            1 -> letOnNullable(random)
            else -> chainedSafeCalls(random)
        }
    }

    private fun safeCallElvis(random: Random): Challenge {
        val isNull = random.nextBoolean()
        val fallback = random.nextInt(-5, 0)

        return if (isNull) {
            val correctValue = IntValue(fallback)
            val snippet = "val s: String? = null\ns?.length ?: $fallback"

            val distractors = distractorGenerator.generateDistractors(
                correctValue = correctValue,
                random = random,
            )
            val options = (distractors + correctValue.display()).shuffled(random)

            Challenge(
                templateKey = key,
                seed = 0,
                snippet = snippet,
                correctAnswer = correctValue.display(),
                options = options,
            )
        } else {
            val words = listOf("kotlin", "hello", "world", "test", "code")
            val word = words[random.nextInt(words.size)]
            val result = word.length
            val correctValue = IntValue(result)

            val snippet = "val s: String? = \"$word\"\ns?.length ?: $fallback"

            val distractors = distractorGenerator.generateDistractors(
                correctValue = correctValue,
                random = random,
            )
            val options = (distractors + correctValue.display()).shuffled(random)

            Challenge(
                templateKey = key,
                seed = 0,
                snippet = snippet,
                correctAnswer = correctValue.display(),
                options = options,
            )
        }
    }

    private fun letOnNullable(random: Random): Challenge {
        val isNull = random.nextBoolean()
        val default = random.nextInt(10, 50)

        return if (isNull) {
            val correctValue = IntValue(default)
            val snippet = "val x: Int? = null\nx?.let { it * 2 } ?: $default"

            val distractors = distractorGenerator.generateDistractors(
                correctValue = correctValue,
                random = random,
            )
            val options = (distractors + correctValue.display()).shuffled(random)

            Challenge(
                templateKey = key,
                seed = 0,
                snippet = snippet,
                correctAnswer = correctValue.display(),
                options = options,
            )
        } else {
            val value = random.nextInt(1, 20)
            val result = value * 2
            val correctValue = IntValue(result)

            val snippet = "val x: Int? = $value\nx?.let { it * 2 } ?: $default"

            val distractors = distractorGenerator.generateDistractors(
                correctValue = correctValue,
                random = random,
            )
            val options = (distractors + correctValue.display()).shuffled(random)

            Challenge(
                templateKey = key,
                seed = 0,
                snippet = snippet,
                correctAnswer = correctValue.display(),
                options = options,
            )
        }
    }

    private fun chainedSafeCalls(random: Random): Challenge {
        val isNull = random.nextBoolean()

        return if (isNull) {
            val correctAnswer = "null"
            val correctValue = NullValue
            val snippet = "val list: List<Int>? = null\nlist?.firstOrNull()?.plus(1)"

            val candidates = sequence {
                yield("0")
                yield("1")
                yield("Exception")
                yield("-1")
            }

            val distractors = candidates
                .filter { it != correctAnswer }
                .distinct()
                .take(3)
                .toList()
            val options = (distractors.take(3) + correctAnswer).shuffled(random)

            Challenge(
                templateKey = key,
                seed = 0,
                snippet = snippet,
                correctAnswer = correctAnswer,
                options = options,
            )
        } else {
            val elements = List(random.nextInt(2, 5)) { random.nextInt(1, 20) }
            val result = elements.first() + 1
            val correctValue = IntValue(result)

            val snippet = "val list: List<Int>? = listOf(${elements.joinToString(", ")})\nlist?.firstOrNull()?.plus(1)"

            val distractors = distractorGenerator.generateDistractors(
                correctValue = correctValue,
                random = random,
            )
            val options = (distractors + correctValue.display()).shuffled(random)

            Challenge(
                templateKey = key,
                seed = 0,
                snippet = snippet,
                correctAnswer = correctValue.display(),
                options = options,
            )
        }
    }
}
