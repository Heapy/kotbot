package io.heapy.kotbot.bot.challenge.templates

import io.heapy.kotbot.bot.challenge.Challenge
import io.heapy.kotbot.bot.challenge.ChallengeTemplate
import io.heapy.kotbot.bot.challenge.DistractorGenerator
import io.heapy.kotbot.bot.challenge.StringValue
import kotlin.random.Random

class StringOperationsTemplate(
    private val distractorGenerator: DistractorGenerator,
) : ChallengeTemplate {
    override val key: String = "string_operations"

    override fun generate(random: Random): Challenge {
        return when (random.nextInt(3)) {
            0 -> substringUppercase(random)
            1 -> replaceReversed(random)
            else -> dropTakePadding(random)
        }
    }

    private fun substringUppercase(random: Random): Challenge {
        val words = listOf("kotlin", "android", "coroutine", "suspend", "lambda", "sequence", "channel", "compose")
        val word = words[random.nextInt(words.size)]
        val start = random.nextInt(0, word.length / 2)
        val end = random.nextInt(start + 2, minOf(start + 5, word.length))

        val result = word.substring(start, end).uppercase()
        val correctValue = StringValue(result)

        val snippet = "\"$word\".substring($start, $end).uppercase()"

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

    private fun replaceReversed(random: Random): Challenge {
        val words = listOf("hello", "world", "kotlin", "code", "data", "value")
        val word = words[random.nextInt(words.size)]
        val charToReplace = word[random.nextInt(word.length)]
        val replacement = ('a'..'z').random(random)

        val result = word.replace(charToReplace, replacement).reversed()
        val correctValue = StringValue(result)

        val snippet = "\"$word\".replace('$charToReplace', '$replacement').reversed()"

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

    private fun dropTakePadding(random: Random): Challenge {
        val words = listOf("programming", "functional", "immutable", "extension", "delegate")
        val word = words[random.nextInt(words.size)]
        val dropCount = random.nextInt(1, 4)
        val takeCount = random.nextInt(2, minOf(5, word.length - dropCount))

        val result = word.drop(dropCount).take(takeCount)
        val correctValue = StringValue(result)

        val snippet = "\"$word\".drop($dropCount).take($takeCount)"

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
