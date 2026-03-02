package io.heapy.kotbot.bot.challenge.templates

import io.heapy.kotbot.bot.challenge.Challenge
import io.heapy.kotbot.bot.challenge.ChallengeTemplate
import io.heapy.kotbot.bot.challenge.DistractorGenerator
import io.heapy.kotbot.bot.challenge.IntValue
import io.heapy.kotbot.bot.challenge.ListValue
import kotlin.random.Random

class MapOperationsTemplate(
    private val distractorGenerator: DistractorGenerator,
) : ChallengeTemplate {
    override val key: String = "map_operations"

    override fun generate(random: Random): Challenge {
        return when (random.nextInt(3)) {
            0 -> filterKeys(random)
            1 -> mapValues(random)
            else -> getOrDefault(random)
        }
    }

    private fun filterKeys(random: Random): Challenge {
        val keys = listOf("a", "b", "c", "d", "e")
        val size = random.nextInt(3, 5)
        val selectedKeys = keys.take(size)
        val values = List(size) { random.nextInt(1, 20) }
        val threshold = values[random.nextInt(values.size)]

        val map = selectedKeys.zip(values).toMap()
        val result = map.filter { it.value > threshold }.keys.sorted()

        val mapLiteral = map.entries.joinToString(", ") { "\"${it.key}\" to ${it.value}" }
        val snippet = "mapOf($mapLiteral).filter { it.value > $threshold }.keys.sorted()"

        val correctValue = ListValue(result.map { io.heapy.kotbot.bot.challenge.StringValue(it) })
        val correctAnswer = correctValue.display()

        val candidates = sequence {
            yield(map.filter { it.value >= threshold }.keys.sorted().toString())
            yield(map.filter { it.value < threshold }.keys.sorted().toString())
            yield(map.keys.sorted().toString())
            yield(map.filter { it.value <= threshold }.keys.sorted().toString())
            if (result.isNotEmpty()) yield(result.dropLast(1).toString())
            if (result.size < selectedKeys.size) yield((result + selectedKeys.first { it !in result }).sorted().toString())
            yield(selectedKeys.take(1).toString())
            yield(selectedKeys.takeLast(1).toString())
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

    private fun mapValues(random: Random): Challenge {
        val keys = listOf("x", "y", "z")
        val values = List(3) { random.nextInt(1, 10) }
        val multiplier = random.nextInt(2, 5)

        val map = keys.zip(values).toMap()
        val result = map.mapValues { it.value * multiplier }.values.sum()
        val correctValue = IntValue(result)

        val mapLiteral = map.entries.joinToString(", ") { "\"${it.key}\" to ${it.value}" }
        val snippet = "mapOf($mapLiteral).mapValues { it.value * $multiplier }.values.sum()"

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

    private fun getOrDefault(random: Random): Challenge {
        val keys = listOf("a", "b", "c")
        val values = List(3) { random.nextInt(1, 20) }
        val lookupKey = listOf("a", "b", "c", "d", "e")[random.nextInt(5)]
        val default = random.nextInt(50, 100)

        val map = keys.zip(values).toMap()
        val result = map.getOrDefault(lookupKey, default)
        val correctValue = IntValue(result)

        val mapLiteral = map.entries.joinToString(", ") { "\"${it.key}\" to ${it.value}" }
        val snippet = "mapOf($mapLiteral).getOrDefault(\"$lookupKey\", $default)"

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
