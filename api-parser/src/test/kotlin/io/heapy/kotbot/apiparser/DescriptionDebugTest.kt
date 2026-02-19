package io.heapy.kotbot.apiparser

import io.heapy.kotbot.apiparser.model.*
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

class DescriptionDebugTest {
    private val json = Json { ignoreUnknownKeys = false }

    @Test
    fun `debug description differences`() {
        val html = loadResource("/api830")
        val expectedJson = loadResource("/api830.json")
        val expected = json.decodeFromString<TelegramApi>(expectedJson)
        val actual = HtmlApiParser.parse(html)

        val expectedByName = expected.methods.associateBy { it.name }
        val actualByName = actual.methods.associateBy { it.name }

        // Show first few description differences
        var count = 0
        for ((name, exp) in expectedByName) {
            val act = actualByName[name] ?: continue

            // Check method description
            if (exp.description != act.description && count < 3) {
                println("=== METHOD DESC: $name ===")
                println("EXPECTED: ${repr(exp.description)}")
                println("ACTUAL:   ${repr(act.description)}")
                println()
                count++
            }

            // Check argument descriptions
            val expArgs = exp.arguments ?: emptyList()
            val actArgs = act.arguments ?: emptyList()
            for (j in expArgs.indices) {
                if (j >= actArgs.size) break
                if (expArgs[j].description != actArgs[j].description && count < 6) {
                    println("=== ARG DESC: $name.${expArgs[j].name} ===")
                    println("EXPECTED: ${repr(expArgs[j].description)}")
                    println("ACTUAL:   ${repr(actArgs[j].description)}")
                    println()
                    count++
                }
            }
        }

        // Also check a few object descriptions
        val expObjByName = expected.objects.associateBy { it.name }
        val actObjByName = actual.objects.associateBy { it.name }

        for ((name, exp) in expObjByName) {
            val act = actObjByName[name] ?: continue
            val expDesc = when (exp) {
                is PropertiesObject -> exp.description
                is AnyOfObject -> exp.description
                is EmptyObject -> exp.description
            }
            val actDesc = when (act) {
                is PropertiesObject -> act.description
                is AnyOfObject -> act.description
                is EmptyObject -> act.description
            }
            if (expDesc != actDesc && count < 10) {
                println("=== OBJECT DESC: $name ===")
                println("EXPECTED: ${repr(expDesc)}")
                println("ACTUAL:   ${repr(actDesc)}")
                println()
                count++
            }
        }
    }

    private fun repr(s: String): String {
        return s.replace("\\", "\\\\")
            .replace("\n", "\\n")
            .replace("\t", "\\t")
    }

    private fun loadResource(path: String): String {
        return this::class.java.getResource(path)?.readText()
            ?: error("Resource not found: $path")
    }
}
