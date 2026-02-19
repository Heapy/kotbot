package io.heapy.kotbot.apiparser

import io.heapy.kotbot.apiparser.model.AnyOfArgument
import io.heapy.kotbot.apiparser.model.AnyOfObject
import io.heapy.kotbot.apiparser.model.Argument
import io.heapy.kotbot.apiparser.model.ArrayArgument
import io.heapy.kotbot.apiparser.model.BooleanArgument
import io.heapy.kotbot.apiparser.model.EmptyObject
import io.heapy.kotbot.apiparser.model.IntArgument
import io.heapy.kotbot.apiparser.model.Method
import io.heapy.kotbot.apiparser.model.Object
import io.heapy.kotbot.apiparser.model.PropertiesObject
import io.heapy.kotbot.apiparser.model.ReferenceArgument
import io.heapy.kotbot.apiparser.model.StringArgument
import io.heapy.kotbot.apiparser.model.TelegramApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class HtmlApiParserTest {
    private val json = Json {
        ignoreUnknownKeys = false
        prettyPrint = true
        explicitNulls = false
        encodeDefaults = true
    }

    @Test
    fun `api830 HTML parses to TelegramApi matching api830 json`() {
        val html = loadResource("/api830")
        val expectedJson = loadResource("/api830.json")
        val expected = json.decodeFromString<TelegramApi>(expectedJson)

        val actual = HtmlApiParser.parse(html)

        // Structural comparison outputting differences
        println("=== Version ===")
        println("Expected: ${expected.version}")
        println("Actual:   ${actual.version}")
        assertEquals(expected.version, actual.version)

        println("\n=== Recent Changes ===")
        println("Expected: ${expected.recent_changes}")
        println("Actual:   ${actual.recent_changes}")
        assertEquals(expected.recent_changes, actual.recent_changes)

        println("\n=== Methods (${expected.methods.size} expected, ${actual.methods.size} actual) ===")
        compareMethods(expected.methods, actual.methods)

        println("\n=== Objects (${expected.objects.size} expected, ${actual.objects.size} actual) ===")
        compareObjects(expected.objects, actual.objects)

        // Final: full JSON tree equality
        val expectedTree = json.parseToJsonElement(expectedJson)
        val actualJson = json.encodeToString(TelegramApi.serializer(), actual)
        val actualTree = json.parseToJsonElement(actualJson)

        // Write both to temp files for diffing
        File("/tmp/expected.json").writeText(json.encodeToString(JsonElement.serializer(), expectedTree))
        File("/tmp/actual.json").writeText(json.encodeToString(JsonElement.serializer(), actualTree))

        assertEquals(expectedTree, actualTree)
    }

    private fun compareMethods(expected: List<Method>, actual: List<Method>) {
        val expectedByName = expected.associateBy { it.name }
        val actualByName = actual.associateBy { it.name }

        val missingMethods = expectedByName.keys - actualByName.keys
        val extraMethods = actualByName.keys - expectedByName.keys

        if (missingMethods.isNotEmpty()) {
            println("MISSING methods: $missingMethods")
        }
        if (extraMethods.isNotEmpty()) {
            println("EXTRA methods: $extraMethods")
        }

        var diffCount = 0
        for ((name, exp) in expectedByName) {
            val act = actualByName[name] ?: continue
            val diffs = mutableListOf<String>()

            if (exp.description != act.description) {
                diffs.add("description differs")
            }
            if (exp.multipart_only != act.multipart_only) {
                diffs.add("multipart_only: expected=${exp.multipart_only}, actual=${act.multipart_only}")
            }
            if (exp.documentation_link != act.documentation_link) {
                diffs.add("documentation_link: expected=${exp.documentation_link}, actual=${act.documentation_link}")
            }
            if (exp.return_type != act.return_type) {
                diffs.add("return_type: expected=${exp.return_type}, actual=${act.return_type}")
            }

            val expArgs = exp.arguments ?: emptyList()
            val actArgs = act.arguments ?: emptyList()
            if (expArgs.size != actArgs.size) {
                diffs.add("arguments count: expected=${expArgs.size}, actual=${actArgs.size}")
            } else {
                for (j in expArgs.indices) {
                    if (expArgs[j] != actArgs[j]) {
                        val expArg = expArgs[j]
                        val actArg = actArgs[j]
                        val argDiffs = mutableListOf<String>()
                        if (expArg.name != actArg.name) argDiffs.add("name")
                        if (expArg.type != actArg.type) argDiffs.add("type: ${expArg.type} vs ${actArg.type}")
                        if (expArg.required != actArg.required) argDiffs.add("required")
                        if (expArg.description != actArg.description) argDiffs.add("description")
                        // Compare type-specific fields
                        argDiffs.addAll(compareArgDetails(expArg, actArg))
                        diffs.add("arg[${expArg.name}]: ${argDiffs.joinToString(", ")}")
                    }
                }
            }

            if ((exp.arguments == null) != (act.arguments == null)) {
                diffs.add("arguments nullability: expected=${exp.arguments == null}, actual=${act.arguments == null}")
            }

            if (diffs.isNotEmpty()) {
                diffCount++
                println("  DIFF method '$name': ${diffs.joinToString("; ")}")
            }
        }
        println("  Total method diffs: $diffCount / ${expected.size}")
    }

    private fun compareArgDetails(expected: Argument, actual: Argument): List<String> {
        val diffs = mutableListOf<String>()
        when {
            expected is IntArgument && actual is IntArgument -> {
                if (expected.default != actual.default) diffs.add("default: ${expected.default} vs ${actual.default}")
                if (expected.min != actual.min) diffs.add("min: ${expected.min} vs ${actual.min}")
                if (expected.max != actual.max) diffs.add("max: ${expected.max} vs ${actual.max}")
                if (expected.enumeration != actual.enumeration) diffs.add("enumeration: ${expected.enumeration} vs ${actual.enumeration}")
            }
            expected is StringArgument && actual is StringArgument -> {
                if (expected.default != actual.default) diffs.add("default: ${expected.default} vs ${actual.default}")
                if (expected.min_len != actual.min_len) diffs.add("min_len: ${expected.min_len} vs ${actual.min_len}")
                if (expected.max_len != actual.max_len) diffs.add("max_len: ${expected.max_len} vs ${actual.max_len}")
                if (expected.enumeration != actual.enumeration) diffs.add("enumeration: ${expected.enumeration} vs ${actual.enumeration}")
            }
            expected is BooleanArgument && actual is BooleanArgument -> {
                if (expected.default != actual.default) diffs.add("default: ${expected.default} vs ${actual.default}")
            }
            expected is ArrayArgument && actual is ArrayArgument -> {
                if (expected.array != actual.array) diffs.add("array: ${expected.array} vs ${actual.array}")
            }
            expected is AnyOfArgument && actual is AnyOfArgument -> {
                if (expected.any_of != actual.any_of) diffs.add("any_of: ${expected.any_of} vs ${actual.any_of}")
            }
            expected is ReferenceArgument && actual is ReferenceArgument -> {
                if (expected.reference != actual.reference) diffs.add("reference: ${expected.reference} vs ${actual.reference}")
            }
        }
        return diffs
    }

    private fun compareObjects(expected: List<Object>, actual: List<Object>) {
        val expectedByName = expected.associateBy { it.name }
        val actualByName = actual.associateBy { it.name }

        val missingObjects = expectedByName.keys - actualByName.keys
        val extraObjects = actualByName.keys - expectedByName.keys

        if (missingObjects.isNotEmpty()) {
            println("MISSING objects: $missingObjects")
        }
        if (extraObjects.isNotEmpty()) {
            println("EXTRA objects: $extraObjects")
        }

        var diffCount = 0
        for ((name, exp) in expectedByName) {
            val act = actualByName[name] ?: continue
            val diffs = mutableListOf<String>()

            if (exp::class != act::class) {
                diffs.add("type: ${exp::class.simpleName} vs ${act::class.simpleName}")
            } else {
                when {
                    exp is PropertiesObject && act is PropertiesObject -> {
                        if (exp.description != act.description) diffs.add("description differs")
                        if (exp.documentation_link != act.documentation_link) diffs.add("documentation_link differs")
                        if (exp.properties.size != act.properties.size) {
                            diffs.add("properties count: ${exp.properties.size} vs ${act.properties.size}")
                        } else {
                            for (j in exp.properties.indices) {
                                if (exp.properties[j] != act.properties[j]) {
                                    val expProp = exp.properties[j]
                                    val actProp = act.properties[j]
                                    val propDiffs = mutableListOf<String>()
                                    if (expProp.name != actProp.name) propDiffs.add("name")
                                    if (expProp.type != actProp.type) propDiffs.add("type: ${expProp.type} vs ${actProp.type}")
                                    if (expProp.required != actProp.required) propDiffs.add("required")
                                    if (expProp.description != actProp.description) propDiffs.add("description")
                                    propDiffs.addAll(compareArgDetails(expProp, actProp))
                                    diffs.add("prop[${expProp.name}]: ${propDiffs.joinToString(", ")}")
                                }
                            }
                        }
                    }
                    exp is AnyOfObject && act is AnyOfObject -> {
                        if (exp.description != act.description) diffs.add("description differs")
                        if (exp.documentation_link != act.documentation_link) diffs.add("documentation_link differs")
                        if (exp.any_of != act.any_of) diffs.add("any_of differs: ${exp.any_of} vs ${act.any_of}")
                    }
                    exp is EmptyObject && act is EmptyObject -> {
                        if (exp.description != act.description) diffs.add("description differs")
                        if (exp.documentation_link != act.documentation_link) diffs.add("documentation_link differs")
                    }
                }
            }

            if (diffs.isNotEmpty()) {
                diffCount++
                println("  DIFF object '$name': ${diffs.joinToString("; ")}")
            }
        }
        println("  Total object diffs: $diffCount / ${expected.size}")
    }

    private fun loadResource(path: String): String {
        return this::class.java.getResource(path)?.readText()
            ?: error("Resource not found: $path")
    }
}
