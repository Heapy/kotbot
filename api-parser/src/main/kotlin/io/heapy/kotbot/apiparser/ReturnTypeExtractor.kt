package io.heapy.kotbot.apiparser

import io.heapy.kotbot.apiparser.model.*
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode

object ReturnTypeExtractor {
    /**
     * Extract return type from method description elements.
     * Looks for patterns like "Returns X", "On success, X is returned", etc.
     */
    fun extract(descriptionElements: List<Element>): ApiType {
        val sentences = parseSentencesFromElements(descriptionElements)

        // Prefer explicit success phrasing before generic "Returns X" because
        // some descriptions start with prose like "Returns the amount of Telegram Stars..."
        // and only later specify the actual API type.
        for (sentence in sentences) {
            val parts = sentence.parts
            if (isKnownFalsePositiveSentence(parts)) continue

            // "On success, X" pattern
            val onSuccessIdx = findWords(parts, listOf("On", "success"))
            if (onSuccessIdx >= 0) {
                val type = extractTypeFromParts(parts, onSuccessIdx + 2)
                if (type != null) return type
            }

            // "Returns X on success" pattern
            val returnsIdx = findWordIgnoreCase(parts, "returns")
            if (returnsIdx != null && hasOnSuccessAfter(parts, returnsIdx)) {
                val type = extractTypeFromParts(parts, returnsIdx + 1)
                if (type != null) return type
            }
        }

        for (sentence in sentences) {
            val parts = sentence.parts

            if (isKnownFalsePositiveSentence(parts)) continue

            // "On success, X" pattern
            val onSuccessIdx = findWords(parts, listOf("On", "success"))
            if (onSuccessIdx >= 0) {
                val type = extractTypeFromParts(parts, onSuccessIdx + 2)
                if (type != null) return type
            }

            // "Returns X" / "returns X"
            val returnsIdx = findWordIgnoreCase(parts, "returns")
            if (returnsIdx != null) {
                val type = extractTypeFromParts(parts, returnsIdx + 1)
                if (type != null) return type
            }

            // "An X" pattern at start
            if (parts.isNotEmpty() && parts[0].text == "An") {
                val type = extractTypeFromParts(parts, 1)
                if (type != null) return type
            }
        }

        error("Could not extract return type from description")
    }

    private fun extractTypeFromParts(parts: List<Part>, startIdx: Int): ApiType? {
        if (startIdx >= parts.size) return null

        // Check for "otherwise" pattern (indicates Or type)
        val otherwiseIdx = parts.subList(startIdx, parts.size).indexOfFirst { it.text == "otherwise" }
        if (otherwiseIdx >= 0) {
            val types = mutableListOf<ApiType>()
            // Collect capitalized words as types
            for (i in startIdx until parts.size) {
                val part = parts[i]
                if (part.text.first().isUpperCase() && part.text != "On" && part.text != "Array") {
                    val typeName = stripPluralEnding(part.text)
                    types.add(TypeParser.parseTypeString(typeName))
                }
            }
            if (types.size > 1) {
                return AnyOfApiType(any_of = types)
            }
        }

        // Check for "Array" or "an array of"
        val anArrayOfIdx = findWords(parts.subList(startIdx, parts.size), listOf("an", "array", "of"))
        if (anArrayOfIdx >= 0) {
            val afterArrayOf = startIdx + anArrayOfIdx + 3
            val innerType = findFirstType(parts, afterArrayOf)
            if (innerType != null) {
                return ArrayApiType(array = innerType)
            }
        }

        // Check if first capitalized word is "Array"
        for (i in startIdx until parts.size) {
            val part = parts[i]
            if (part.text == "Array") {
                // "Array of X"
                val innerType = findFirstType(parts, i + 1) // skip "of" and find type
                if (innerType != null) {
                    return ArrayApiType(array = innerType)
                }
            }
            if (part.text.first().isUpperCase() && part.text != "Array" && part.text != "On") {
                val typeName = stripPluralEnding(part.text)
                return TypeParser.parseTypeString(typeName)
            }
        }

        return null
    }

    private fun findFirstType(parts: List<Part>, startIdx: Int): ApiType? {
        for (i in startIdx until parts.size) {
            val part = parts[i]
            if (part.text.first().isUpperCase()) {
                val typeName = stripPluralEnding(part.text)
                if (typeName == "Array") {
                    // Nested array
                    val innerType = findFirstType(parts, i + 1)
                    if (innerType != null) {
                        return ArrayApiType(array = innerType)
                    }
                }
                return TypeParser.parseTypeString(typeName)
            }
        }
        return null
    }

    private fun stripPluralEnding(s: String): String {
        return if (s.endsWith("es")) {
            s.dropLast(1)
        } else {
            s
        }
    }

    private fun matchesWords(parts: List<Part>, words: List<String>): Boolean {
        if (parts.size < words.size) return false
        return words.indices.all { parts[it].text == words[it] }
    }

    private fun isKnownFalsePositiveSentence(parts: List<Part>): Boolean {
        // Skip "Returns the bot's Telegram ..."
        if (matchesWords(parts, listOf("Returns", "the", "bot's", "Telegram"))) return true
        // Skip "Returns the list of ..." (this is a false positive)
        if (matchesWords(parts, listOf("Returns", "the", "list", "of"))) return true
        return false
    }

    private fun hasOnSuccessAfter(parts: List<Part>, fromIdx: Int): Boolean {
        if (fromIdx + 1 >= parts.size) return false
        val tail = parts.subList(fromIdx + 1, parts.size)
        return findWords(tail, listOf("on", "success")) >= 0
    }

    private fun findWords(parts: List<Part>, words: List<String>): Int {
        for (i in 0..parts.size - words.size) {
            if (words.indices.all { parts[i + it].text.equals(words[it], ignoreCase = true) }) {
                return i
            }
        }
        return -1
    }

    private fun findWordIgnoreCase(parts: List<Part>, word: String): Int? {
        return parts.indexOfFirst { it.text.equals(word, ignoreCase = true) }.takeIf { it >= 0 }
    }

    data class Sentence(val parts: List<Part>)

    data class Part(
        val text: String,
        val isType: Boolean = false, // starts with uppercase
    )

    private fun parseSentencesFromElements(elements: List<Element>): List<Sentence> {
        val allParts = mutableListOf<Part>()
        val sentences = mutableListOf<Sentence>()
        var parenDepth = 0

        fun flushSentence() {
            if (allParts.isNotEmpty()) {
                sentences.add(Sentence(allParts.toList()))
                allParts.clear()
            }
        }

        fun processNode(node: Node) {
            when (node) {
                is TextNode -> {
                    val text = node.wholeText
                    var i = 0
                    while (i < text.length) {
                        val ch = text[i]
                        when {
                            ch == '(' -> { parenDepth++; i++ }
                            ch == ')' -> { parenDepth = maxOf(0, parenDepth - 1); i++ }
                            parenDepth > 0 -> i++
                            ch == '.' -> { flushSentence(); i++ }
                            ch == ' ' || ch == ',' || ch == '\n' -> i++
                            else -> {
                                val end = findWordEnd(text, i)
                                val word = text.substring(i, end)
                                allParts.add(Part(word))
                                i = end
                            }
                        }
                    }
                }
                is Element -> {
                    if (parenDepth > 0) return
                    when (node.tagName()) {
                        "a", "em", "code", "strong", "b", "i" -> {
                            val text = node.text()
                            if (text.isNotBlank()) {
                                text.split(" ").filter { it.isNotBlank() }.forEach { word ->
                                    allParts.add(Part(word))
                                }
                            }
                        }
                        "img" -> {
                            val alt = node.attr("alt")
                            if (alt.isNotEmpty()) {
                                allParts.add(Part(alt))
                            }
                        }
                        "br" -> {}
                        "li" -> {
                            flushSentence()
                            for (child in node.childNodes()) {
                                processNode(child)
                            }
                        }
                        else -> {
                            for (child in node.childNodes()) {
                                processNode(child)
                            }
                        }
                    }
                }
            }
        }

        for (element in elements) {
            for (child in element.childNodes()) {
                processNode(child)
            }
        }
        flushSentence()

        return sentences
    }

    private fun findWordEnd(text: String, start: Int): Int {
        var i = start
        while (i < text.length) {
            val ch = text[i]
            if (ch == ' ' || ch == ',' || ch == '.' || ch == '(' || ch == ')' || ch == '\n') break
            i++
        }
        return i
    }
}
