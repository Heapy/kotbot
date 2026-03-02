package io.heapy.kotbot.bot.challenge

import io.heapy.kotbot.bot.challenge.templates.CollectionPipelineTemplate
import io.heapy.kotbot.bot.challenge.templates.MapOperationsTemplate
import io.heapy.kotbot.bot.challenge.templates.NullSafetyTemplate
import io.heapy.kotbot.bot.challenge.templates.PairDestructuringTemplate
import io.heapy.kotbot.bot.challenge.templates.RangeExpressionTemplate
import io.heapy.kotbot.bot.challenge.templates.ScopeTransformTemplate
import io.heapy.kotbot.bot.challenge.templates.StringOperationsTemplate
import io.heapy.kotbot.bot.join.createJoinChallengeModule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ChallengeGeneratorTest {
    @Test
    fun `seed produces reproducible challenges`() {
        val joinChallengeModule = createJoinChallengeModule {}
        val generator = joinChallengeModule.challengeGenerator
        val seed = 42L
        val challenge1 = generator.generate(seed)
        val challenge2 = generator.generate(seed)

        assertEquals(challenge1.templateKey, challenge2.templateKey)
        assertEquals(challenge1.snippet, challenge2.snippet)
        assertEquals(challenge1.correctAnswer, challenge2.correctAnswer)
        assertEquals(challenge1.options, challenge2.options)
    }

    @Test
    fun `different seeds produce different challenges`() {
        val joinChallengeModule = createJoinChallengeModule {}
        val generator = joinChallengeModule.challengeGenerator
        val challenges = (1L..20L).map { generator.generate(it) }
        val uniqueSnippets = challenges.map { it.snippet }.toSet()
        assertTrue(uniqueSnippets.size > 1, "Expected different seeds to produce different challenges")
    }

    @Test
    fun `all templates generate valid challenges`() {
        val joinChallengeModule = createJoinChallengeModule {}
        val templates = joinChallengeModule.challengeTemplates
        assertTrue(templates.size == 7, "Expected 7 templates, got ${templates.size}")

        for (template in templates) {
            repeat(10) { i ->
                val challenge = template.generate(kotlin.random.Random(i.toLong()))
                assertNotNull(challenge.snippet, "Snippet should not be null for ${template.key}")
                assertNotNull(challenge.correctAnswer, "Correct answer should not be null for ${template.key}")
                assertTrue(
                    challenge.options.size >= 4,
                    "Expected at least 4 options for ${template.key}, got ${challenge.options.size}"
                )
                assertTrue(
                    challenge.correctAnswer in challenge.options,
                    "Correct answer '${challenge.correctAnswer}' should be in options ${challenge.options} for ${template.key}"
                )
            }
        }
    }

    @Test
    fun `generated challenge has unique options`() {
        val joinChallengeModule = createJoinChallengeModule {}
        val generator = joinChallengeModule.challengeGenerator
        repeat(50) {
            val challenge = generator.generate()
            assertTrue(challenge.options.size >= 4, "Expected at least 4 options")
            assertEquals(
                challenge.options.size, challenge.options.toSet().size,
                "Options should be unique: ${challenge.options}"
            )
        }
    }

    @Test
    fun `collection pipeline template`() {
        val joinChallengeModule = createJoinChallengeModule {}
        val distractorGenerator = joinChallengeModule.distractorGenerator
        val template = CollectionPipelineTemplate(distractorGenerator)
        repeat(20) { i ->
            val challenge = template.generate(kotlin.random.Random(i.toLong()))
            assertTrue(challenge.templateKey == "collection_pipeline")
            assertTrue(challenge.options.contains(challenge.correctAnswer))
        }
    }

    @Test
    fun `string operations template`() {
        val joinChallengeModule = createJoinChallengeModule {}
        val distractorGenerator = joinChallengeModule.distractorGenerator
        val template = StringOperationsTemplate(distractorGenerator)
        repeat(20) { i ->
            val challenge = template.generate(kotlin.random.Random(i.toLong()))
            assertTrue(challenge.templateKey == "string_operations")
            assertTrue(challenge.options.contains(challenge.correctAnswer))
        }
    }

    @Test
    fun `scope transform template`() {
        val joinChallengeModule = createJoinChallengeModule {}
        val distractorGenerator = joinChallengeModule.distractorGenerator
        val template = ScopeTransformTemplate(distractorGenerator)
        repeat(20) { i ->
            val challenge = template.generate(kotlin.random.Random(i.toLong()))
            assertTrue(challenge.templateKey == "scope_transform")
            assertTrue(challenge.options.contains(challenge.correctAnswer))
        }
    }

    @Test
    fun `map operations template`() {
        val joinChallengeModule = createJoinChallengeModule {}
        val distractorGenerator = joinChallengeModule.distractorGenerator
        val template = MapOperationsTemplate(distractorGenerator)
        repeat(20) { i ->
            val challenge = template.generate(kotlin.random.Random(i.toLong()))
            assertTrue(challenge.templateKey == "map_operations")
            assertTrue(challenge.options.contains(challenge.correctAnswer))
        }
    }

    @Test
    fun `range expression template`() {
        val joinChallengeModule = createJoinChallengeModule {}
        val distractorGenerator = joinChallengeModule.distractorGenerator
        val template = RangeExpressionTemplate(distractorGenerator)
        repeat(20) { i ->
            val challenge = template.generate(kotlin.random.Random(i.toLong()))
            assertTrue(challenge.templateKey == "range_expression")
            assertTrue(challenge.options.contains(challenge.correctAnswer))
        }
    }

    @Test
    fun `pair destructuring template`() {
        val joinChallengeModule = createJoinChallengeModule {}
        val distractorGenerator = joinChallengeModule.distractorGenerator
        val template = PairDestructuringTemplate(distractorGenerator)
        repeat(20) { i ->
            val challenge = template.generate(kotlin.random.Random(i.toLong()))
            assertTrue(challenge.templateKey == "pair_destructuring")
            assertTrue(challenge.options.contains(challenge.correctAnswer))
        }
    }

    @Test
    fun `null safety template`() {
        val joinChallengeModule = createJoinChallengeModule {}
        val distractorGenerator = joinChallengeModule.distractorGenerator
        val template = NullSafetyTemplate(distractorGenerator)
        repeat(20) { i ->
            val challenge = template.generate(kotlin.random.Random(i.toLong()))
            assertTrue(challenge.templateKey == "null_safety")
            assertTrue(challenge.options.contains(challenge.correctAnswer))
        }
    }
}
