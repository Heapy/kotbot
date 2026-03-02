package io.heapy.kotbot.bot.challenge

import kotlin.random.Random

class DistractorGenerator {
    fun generateDistractors(
        correctValue: KotlinValue,
        random: Random,
        count: Int = 9,
    ): List<String> {
        val generators = distractorStrategies(correctValue, random)

        val candidates = sequence {
            while (true) {
                for (generator in generators) {
                    yield(generator())
                }
            }
        }

        return candidates
            .filter { it != correctValue }
            .distinct()
            .take(count)
            .map { it.display() }
            .toList()
    }

    private fun distractorStrategies(
        value: KotlinValue,
        random: Random,
    ): List<() -> KotlinValue> = when (value) {
        is IntValue -> listOf(
            { IntValue(value.value + 1) },
            { IntValue(value.value - 1) },
            { IntValue(value.value + random.nextInt(2, 10)) },
            { IntValue(value.value - random.nextInt(2, 10)) },
            { IntValue(value.value * 2) },
            { IntValue(value.value * random.nextInt(2, 10)) },
            { IntValue(0) },
        )

        is StringValue -> listOf(
            { StringValue(value.value.reversed()) },
            { StringValue(value.value.uppercase()) },
            { StringValue(value.value.lowercase()) },
            {
                if (value.value.length > 1) {
                    val i = random.nextInt(value.value.length - 1)
                    val chars = value.value.toCharArray()
                    val tmp = chars[i]
                    chars[i] = chars[i + 1]
                    chars[i + 1] = tmp
                    StringValue(String(chars))
                } else {
                    StringValue(value.value + "x")
                }
            },
            {
                if (value.value.isNotEmpty()) {
                    StringValue(value.value.substring(0, value.value.length - 1))
                } else {
                    StringValue("x")
                }
            },
            {
                if (value.value.isNotEmpty()) {
                    val i = random.nextInt(value.value.length)
                    val c = ('a'..'z').random(random)
                    val chars = value.value.toCharArray()
                    chars[i] = c
                    StringValue(String(chars))
                } else {
                    StringValue(('a'..'z').random(random).toString())
                }
            },
            {
                val i = random.nextInt(value.value.length + 1)
                val c = ('a'..'z').random(random)
                StringValue(value.value.substring(0, i) + c + value.value.substring(i))
            },
        )

        is BooleanValue -> listOf(
            { BooleanValue(!value.value) },
            { NullValue },
            { IntValue(1) },
            { IntValue(0) },
            { StringValue("True") },
            { StringValue("False") },
            { StringValue(".TRUE.") },
            { StringValue(".FALSE.") },
            { StringValue("yes") },
            { StringValue("no") },
            { StringValue("nil") },
            { StringValue("on") },
            { StringValue("off") },
        )

        is ListValue -> listOf(
            {
                if (value.elements.size > 1) {
                    ListValue(value.elements.reversed())
                } else {
                    ListValue(value.elements + IntValue(0))
                }
            },
            {
                if (value.elements.isNotEmpty()) {
                    ListValue(value.elements.drop(1))
                } else {
                    ListValue(listOf(IntValue(0)))
                }
            },
            {
                if (value.elements.size >= 2) {
                    val shuffled = value.elements.toMutableList()
                    val i = random.nextInt(shuffled.size - 1)
                    val tmp = shuffled[i]
                    shuffled[i] = shuffled[i + 1]
                    shuffled[i + 1] = tmp
                    ListValue(shuffled)
                } else {
                    ListValue(value.elements + IntValue(random.nextInt(1, 10)))
                }
            },
            {
                if (value.elements.isNotEmpty()) {
                    ListValue(value.elements.dropLast(1))
                } else {
                    ListValue(listOf(IntValue(1)))
                }
            },
            {
                if (value.elements.isNotEmpty()) {
                    val modified = value.elements.toMutableList()
                    modified[random.nextInt(modified.size)] = IntValue(random.nextInt(-10, 50))
                    ListValue(modified)
                } else {
                    ListValue(listOf(IntValue(random.nextInt(-10, 50))))
                }
            },
            {
                ListValue(value.elements + IntValue(random.nextInt(-10, 50)))
            },
        )

        is MapValue -> listOf(
            {
                if (value.entries.isNotEmpty()) {
                    MapValue(value.entries.toList().drop(1).toMap())
                } else {
                    MapValue(mapOf(StringValue("k") to IntValue(0)))
                }
            },
            {
                MapValue(value.entries.toList().reversed().toMap())
            },
            {
                if (value.entries.size >= 2) {
                    val entries = value.entries.toList().toMutableList()
                    val i = random.nextInt(entries.size - 1)
                    val tmp = entries[i]
                    entries[i] = entries[i + 1]
                    entries[i + 1] = tmp
                    MapValue(entries.toMap())
                } else {
                    MapValue(emptyMap())
                }
            },
            {
                if (value.entries.isNotEmpty()) {
                    val entries = value.entries.toList().toMutableList()
                    val i = random.nextInt(entries.size)
                    entries[i] = entries[i].first to IntValue(random.nextInt(-10, 50))
                    MapValue(entries.toMap())
                } else {
                    MapValue(mapOf(StringValue("k") to IntValue(random.nextInt(-10, 50))))
                }
            },
            {
                if (value.entries.isNotEmpty()) {
                    MapValue(value.entries.toList().dropLast(1).toMap())
                } else {
                    MapValue(mapOf(StringValue("x") to IntValue(0)))
                }
            },
        )

        is PairValue -> {
            val firstStrategies = distractorStrategies(value.first, random)
            val secondStrategies = distractorStrategies(value.second, random)
            listOf(
                {
                    PairValue(value.second, value.first)
                },
                {
                    val gen = firstStrategies[random.nextInt(firstStrategies.size)]
                    val newFirst = gen()
                    PairValue(newFirst, value.second)
                },
                {
                    val gen = secondStrategies[random.nextInt(secondStrategies.size)]
                    val newSecond = gen()
                    PairValue(value.first, newSecond)
                },
            )
        }

        is NullValue -> listOf(
            { IntValue(0) },
            { IntValue(-1) },
            { StringValue("") },
            { BooleanValue(false) },
            { StringValue("NULL") },
            { StringValue("undefined") },
            { StringValue("nil") },
            { StringValue("NIL") },
            { StringValue("None") },
            { StringValue("NaN") },
        )
    }
}
