package io.heapy.kotbot.bot.challenge

import io.heapy.kotbot.bot.challenge.templates.CollectionPipelineTemplate
import io.heapy.kotbot.bot.challenge.templates.MapOperationsTemplate
import io.heapy.kotbot.bot.challenge.templates.NullSafetyTemplate
import io.heapy.kotbot.bot.challenge.templates.PairDestructuringTemplate
import io.heapy.kotbot.bot.challenge.templates.RangeExpressionTemplate
import io.heapy.kotbot.bot.challenge.templates.ScopeTransformTemplate
import io.heapy.kotbot.bot.challenge.templates.StringOperationsTemplate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import java.util.concurrent.TimeUnit
import kotlin.random.Random

@Timeout(value = 1, unit = TimeUnit.SECONDS)
class DistractorGeneratorTest {
    private val distractorGenerator = DistractorGenerator()

    @Test
    fun `generates 9 unique distractors for IntValue`() {
        val correct = IntValue(42)
        val distractors = distractorGenerator.generateDistractors(
            correctValue = correct,
            random = Random(1),
        )
        assertEquals(9, distractors.size)
        assertEquals(9, distractors.toSet().size, "Distractors should be unique")
        assertFalse(correct.display() in distractors, "Correct answer should not be in distractors")
    }

    @Test
    fun `generates 9 unique distractors for StringValue`() {
        val correct = StringValue("kotlin")
        val distractors = distractorGenerator.generateDistractors(
            correctValue = correct,
            random = Random(1),
        )
        assertEquals(9, distractors.size)
        assertEquals(9, distractors.toSet().size, "Distractors should be unique")
        assertFalse(correct.display() in distractors, "Correct answer should not be in distractors")
    }

    @Test
    fun `generates 9 unique distractors for BooleanValue`() {
        val correct = BooleanValue(true)
        val distractors = distractorGenerator.generateDistractors(
            correctValue = correct,
            random = Random(1),
        )
        assertEquals(9, distractors.size)
        assertFalse(correct.display() in distractors, "Correct answer should not be in distractors")
    }

    @Test
    fun `generates 9 unique distractors for ListValue`() {
        val correct = ListValue(listOf(IntValue(1), IntValue(2), IntValue(3)))
        val distractors = distractorGenerator.generateDistractors(
            correctValue = correct,
            random = Random(1),
        )
        assertEquals(9, distractors.size)
        assertFalse(correct.display() in distractors, "Correct answer should not be in distractors")
    }

    @Test
    fun `generates 9 unique distractors for NullValue`() {
        val correct = NullValue
        val distractors = distractorGenerator.generateDistractors(
            correctValue = correct,
            random = Random(1),
        )
        assertEquals(9, distractors.size)
        assertFalse(correct.display() in distractors, "Correct answer should not be in distractors")
    }

    @Test
    fun `generates 9 unique distractors for MapValue`() {
        val correct = MapValue(mapOf(StringValue("a") to IntValue(1), StringValue("b") to IntValue(2)))
        val distractors = distractorGenerator.generateDistractors(
            correctValue = correct,
            random = Random(1),
        )
        assertEquals(9, distractors.size)
        assertEquals(9, distractors.toSet().size, "Distractors should be unique")
        assertFalse(correct.display() in distractors, "Correct answer should not be in distractors")
    }

    @Test
    fun `generates 9 unique distractors for PairValue`() {
        val correct = PairValue(IntValue(3), IntValue(7))
        val distractors = distractorGenerator.generateDistractors(
            correctValue = correct,
            random = Random(1),
        )
        assertEquals(9, distractors.size)
        assertEquals(9, distractors.toSet().size, "Distractors should be unique")
        assertFalse(correct.display() in distractors, "Correct answer should not be in distractors")
    }

    @Test
    fun `distractors are stable across calls with same seed`() {
        val correct = IntValue(10)
        val d1 = distractorGenerator.generateDistractors(correct, Random(42))
        val d2 = distractorGenerator.generateDistractors(correct, Random(42))
        assertEquals(d1, d2)
    }

    @Test
    fun `all templates produce challenges where correct answer is not among distractors`() {
        val templates = listOf(
            CollectionPipelineTemplate(distractorGenerator),
            StringOperationsTemplate(distractorGenerator),
            ScopeTransformTemplate(distractorGenerator),
            MapOperationsTemplate(distractorGenerator),
            RangeExpressionTemplate(distractorGenerator),
            PairDestructuringTemplate(distractorGenerator),
            NullSafetyTemplate(distractorGenerator),
        )
        for (template in templates) {
            repeat(10) { i ->
                val challenge = template.generate(Random(i.toLong()))
                val correctAnswer = challenge.correctAnswer
                val otherOptions = challenge.options.filter { it != correctAnswer }
                assertTrue(
                    otherOptions.size >= 3,
                    "Expected at least 3 distractors for ${template.key}, got ${otherOptions.size}. Options: ${challenge.options}"
                )
                assertTrue(
                    otherOptions.none { it == correctAnswer },
                    "Correct answer should not appear among distractors for ${template.key}"
                )
            }
        }
    }
}
