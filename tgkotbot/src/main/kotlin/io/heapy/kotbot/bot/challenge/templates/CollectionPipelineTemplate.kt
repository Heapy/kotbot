package io.heapy.kotbot.bot.challenge.templates

import io.heapy.kotbot.bot.challenge.Challenge
import io.heapy.kotbot.bot.challenge.ChallengeTemplate
import io.heapy.kotbot.bot.challenge.DistractorGenerator
import io.heapy.kotbot.bot.challenge.IntValue
import io.heapy.kotbot.bot.challenge.ListValue
import kotlin.random.Random

class CollectionPipelineTemplate(
    private val distractorGenerator: DistractorGenerator,
) : ChallengeTemplate {
    override val key: String = "collection_pipeline"

    override fun generate(random: Random): Challenge {
        return when (random.nextInt(3)) {
            0 -> filterMapSum(random)
            1 -> filterCount(random)
            else -> mapDistinct(random)
        }
    }

    private fun filterMapSum(random: Random): Challenge {
        val size = random.nextInt(4, 7)
        val elements = List(size) { random.nextInt(1, 20) }
        val threshold = elements[random.nextInt(elements.size)]

        @Suppress("SimplifiableCallChain")
        val result = elements.filter { it > threshold }.map { it * 2 }.sum()
        val correctValue = IntValue(result)

        val snippet = "listOf(${elements.joinToString(", ")}).filter { it > $threshold }.map { it * 2 }.sum()"

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

    private fun filterCount(random: Random): Challenge {
        val size = random.nextInt(5, 8)
        val elements = List(size) { random.nextInt(1, 30) }
        val modulo = random.nextInt(2, 5)

        val result = elements.filter { it % modulo == 0 }.size
        val correctValue = IntValue(result)

        val snippet = "listOf(${elements.joinToString(", ")}).filter { it % $modulo == 0 }.size"

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

    private fun mapDistinct(random: Random): Challenge {
        val size = random.nextInt(5, 8)
        val elements = List(size) { random.nextInt(1, 10) }
        val divisor = random.nextInt(2, 4)

        val result = elements.map { it / divisor }.distinct().sorted()
        val correctValue = ListValue(result.map { IntValue(it) })
        val correctAnswer = correctValue.display()

        val snippet = "listOf(${elements.joinToString(", ")}).map { it / $divisor }.distinct().sorted()"

        val candidates = sequence {
            yield(elements.map { it / divisor }.sorted().toString())
            yield(elements.map { it / divisor }.distinct().toString())
            yield(elements.map { it % divisor }.distinct().sorted().toString())
            val extra = result.toMutableList()
            if (extra.isNotEmpty()) {
                extra.add(extra.last() + 1)
            } else {
                extra.add(0)
            }
            yield(extra.toString())
        }

        val distractors = candidates
            .filter { it != correctAnswer }
            .distinct()
            .take(3)
            .toList()

        val options = (distractors.take(3) + correctAnswer).shuffled(random)

        return Challenge(
            templateKey = key,
            seed = 0,
            snippet = snippet,
            correctAnswer = correctAnswer,
            options = options,
        )
    }
}
