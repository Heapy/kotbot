package io.heapy.kotbot.apiparser

import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode

data class Constraints(
    val defaultValue: String? = null,
    val min: String? = null,
    val max: String? = null,
    val intEnumeration: List<Int>? = null,
    val stringEnumeration: List<String>? = null,
)

object DescriptionParser {
    /**
     * Extract constraints (defaults, min/max, enumerations) from a description element.
     * Mirrors the Rust parser's sentence-based constraint extraction.
     */
    fun extractConstraints(element: Element): Constraints {
        val sentences = parseSentences(element)

        val defaultValue = extractDefault(sentences)
        val minMax = extractMinMax(sentences)
        val enumeration = extractEnumeration(sentences)

        return Constraints(
            defaultValue = defaultValue,
            min = minMax?.first,
            max = minMax?.second,
            intEnumeration = enumeration?.mapNotNull { it.toIntOrNull() }?.takeIf { e ->
                e.size == enumeration.size
            },
            stringEnumeration = enumeration?.takeIf { e ->
                e.any { it.toIntOrNull() == null }
            },
        )
    }

    /**
     * Parse text into sentences, handling parentheses and quotes like the Rust parser.
     */
    private fun parseSentences(element: Element): List<Sentence> {
        val parts = mutableListOf<Part>()
        val sentences = mutableListOf<Sentence>()
        var parenDepth = 0
        var inQuote = false
        var quoteStartIdx = 0

        fun flushSentence() {
            if (parts.isNotEmpty()) {
                sentences.add(Sentence(parts.toList()))
                parts.clear()
            }
        }

        fun processTextNode(text: String, nodeKind: PartKind = PartKind.Word, isInlineElement: Boolean = false) {
            // Tokenize text into words, handling quotes and parentheses
            var i = 0
            while (i < text.length) {
                val ch = text[i]
                when {
                    ch == '(' -> {
                        parenDepth++
                        i++
                    }
                    ch == ')' -> {
                        parenDepth = maxOf(0, parenDepth - 1)
                        i++
                    }
                    parenDepth > 0 -> {
                        i++
                    }
                    ch == '"' || ch == '\u201c' || ch == '\u201d' -> {
                        if (!inQuote) {
                            inQuote = true
                            quoteStartIdx = parts.size
                        } else {
                            // Closing quote - merge parts since quote start
                            val quoteParts = parts.subList(quoteStartIdx, parts.size)
                            val quoted = quoteParts.joinToString(" ") { it.text }
                            quoteParts.clear()
                            parts.add(Part(quoted, hasQuotes = true))
                            inQuote = false
                        }
                        i++
                    }
                    // Don't split on '.' when inside styled inline elements (bold, italic, code)
                    // because the Rust parser creates Parts directly for these without going through the lexer
                    ch == '.' && !inQuote && !isInlineElement -> {
                        flushSentence()
                        i++
                    }
                    ch == ',' || ch == ' ' || ch == '\n' -> {
                        i++
                    }
                    // In inline elements, '.' is part of the word (e.g., ".WEBP")
                    ch == '.' && isInlineElement -> {
                        val wordEnd = findWordEndInline(text, i)
                        val word = text.substring(i, wordEnd)
                        if (word.isNotEmpty()) {
                            parts.add(Part(word, kind = nodeKind))
                        }
                        i = wordEnd
                    }
                    else -> {
                        val wordEnd = findWordEnd(text, i)
                        val word = text.substring(i, wordEnd)
                        parts.add(Part(word, kind = nodeKind))
                        i = wordEnd
                    }
                }
            }
        }

        fun processNode(node: Node) {
            if (parenDepth > 0 && node !is TextNode) return

            when (node) {
                is TextNode -> {
                    processTextNode(node.wholeText)
                }
                is Element -> {
                    when (node.tagName()) {
                        "em", "i" -> {
                            val text = node.text()
                            processTextNode(text, PartKind.Italic, isInlineElement = true)
                        }
                        "code" -> {
                            val text = node.text()
                            processTextNode(text, PartKind.Code, isInlineElement = true)
                        }
                        "strong", "b" -> {
                            val text = node.text()
                            processTextNode(text, PartKind.Bold, isInlineElement = true)
                        }
                        "a" -> {
                            val text = node.text()
                            val href = node.attr("href")
                            processTextNode(text, PartKind.Link(href))
                        }
                        "img" -> {
                            val alt = node.attr("alt")
                            if (alt.isNotEmpty()) {
                                parts.add(Part(alt, hasQuotes = inQuote))
                            }
                        }
                        "br" -> { /* ignore */ }
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

        for (child in element.childNodes()) {
            processNode(child)
        }
        flushSentence()

        return sentences
    }

    private fun findWordEnd(text: String, start: Int): Int {
        var i = start
        while (i < text.length) {
            val ch = text[i]
            if (ch == ' ' || ch == ',' || ch == '.' || ch == '(' || ch == ')' ||
                ch == '\n' || ch == '"' || ch == '\u201c' || ch == '\u201d') {
                break
            }
            i++
        }
        return i
    }

    /**
     * Find word end in inline elements where '.' is part of the word (e.g., ".WEBP").
     */
    private fun findWordEndInline(text: String, start: Int): Int {
        var i = start
        while (i < text.length) {
            val ch = text[i]
            if (ch == ' ' || ch == ',' || ch == '(' || ch == ')' ||
                ch == '\n' || ch == '"' || ch == '\u201c' || ch == '\u201d') {
                break
            }
            i++
        }
        return i
    }

    private fun extractDefault(sentences: List<Sentence>): String? {
        // Mirrors the Rust parser's pattern matching for defaults.
        // Important: "Defaults to" (capitalized) extracts a default,
        // but "defaults to" (lowercase) causes the sentence to be EXCLUDED
        // (skipped entirely), mimicking the Rust exclude pattern behavior.
        sentenceLoop@ for (sentence in sentences) {
            val parts = sentence.parts

            // First check for exclude patterns (lowercase "defaults to" excludes sentence)
            for (i in 0 until parts.size - 1) {
                if (parts[i].text == "defaults" && parts[i + 1].text == "to") {
                    continue@sentenceLoop // exclude this sentence
                }
            }

            // "Defaults to X" (capitalized)
            for (i in 0 until parts.size - 1) {
                if (parts[i].text == "Defaults" && parts[i + 1].text == "to") {
                    val valueIdx = i + 2
                    if (valueIdx < parts.size) {
                        return parts[valueIdx].text
                    }
                }
            }

            // "must be X" where X is italic
            for (i in 0 until parts.size - 1) {
                if (parts[i].text == "must" && i + 1 < parts.size && parts[i + 1].text == "be") {
                    val valueIdx = i + 2
                    if (valueIdx < parts.size && parts[valueIdx].kind == PartKind.Italic) {
                        return parts[valueIdx].text
                    }
                }
            }

            // "always "X""
            for (i in parts.indices) {
                if (parts[i].text == "always" && i + 1 < parts.size && parts[i + 1].hasQuotes) {
                    return parts[i + 1].text
                }
            }
        }

        return null
    }

    private fun extractMinMax(sentences: List<Sentence>): Pair<String, String>? {
        // Pattern: "Values between X-Y"
        for (sentence in sentences) {
            val parts = sentence.parts
            for (i in 0 until parts.size - 1) {
                if (parts[i].textLower == "values" && i + 1 < parts.size && parts[i + 1].textLower == "between") {
                    val valueIdx = i + 2
                    if (valueIdx < parts.size) {
                        val range = parts[valueIdx].text
                        val split = range.split('-')
                        if (split.size == 2) {
                            return split[0] to split[1]
                        }
                    }
                }
            }
        }

        // Pattern: "X-Y characters"
        for (sentence in sentences) {
            val parts = sentence.parts
            for (i in parts.indices) {
                if (parts[i].textLower == "characters" && i >= 1) {
                    val rangeIdx = i - 1
                    val range = parts[rangeIdx].text
                    val split = range.split('-')
                    if (split.size == 2 && split[0].all { it.isDigit() } && split[1].all { it.isDigit() }) {
                        return split[0] to split[1]
                    }
                }
            }
        }

        return null
    }

    private fun extractEnumeration(sentences: List<Sentence>): List<String>? {
        // Patterns for "one of", "either", "Can be", etc.
        for (sentence in sentences) {
            val parts = sentence.parts

            // "One of" / "one of"
            for (i in 0 until parts.size - 1) {
                if ((parts[i].textLower == "one" || parts[i].textLower == "either") &&
                    i + 1 < parts.size && parts[i + 1].textLower == "of") {
                    return extractQuotedOrItalicValues(parts, i + 2)
                }
                if (parts[i].textLower == "either") {
                    return extractQuotedOrItalicValues(parts, i + 1)
                }
            }

            // "Can be" - only when followed by a quoted value
            for (i in 0 until parts.size - 2) {
                if (parts[i].textLower == "can" && parts[i + 1].textLower == "be" &&
                    parts[i + 2].hasQuotes) {
                    return extractQuotedOrItalicValues(parts, i + 2)
                }
            }

            // "Choose one" pattern
            for (i in 0 until parts.size - 1) {
                if (parts[i].textLower == "choose" && i + 1 < parts.size && parts[i + 1].textLower == "one") {
                    return extractQuotedOrItalicValues(parts, i + 2)
                }
            }

            // Direct quoted pattern: "X" or "Y"
            for (i in 0 until parts.size - 2) {
                if (parts[i].hasQuotes && parts[i + 1].textLower == "or" && parts[i + 2].hasQuotes) {
                    return extractQuotedOrItalicValues(parts, i)
                }
            }
        }

        return null
    }

    private fun extractQuotedOrItalicValues(parts: List<Part>, startIdx: Int): List<String>? {
        val values = mutableListOf<String>()
        for (i in startIdx until parts.size) {
            val part = parts[i]
            if (part.hasQuotes || part.kind == PartKind.Italic ||
                part.text.all { it.isDigit() }) {
                // Skip URL-like values that are not actual enum values
                if (part.text.contains("://") || part.text.contains("attach:")) continue
                if (part.text !in values) {
                    values.add(part.text)
                }
            }
        }
        return values.ifEmpty { null }
    }

    data class Sentence(val parts: List<Part>)

    data class Part(
        val text: String,
        val hasQuotes: Boolean = false,
        val kind: PartKind = PartKind.Word,
    ) {
        val textLower = text.lowercase()
    }

    sealed interface PartKind {
        data object Word : PartKind
        data object Italic : PartKind
        data object Code : PartKind
        data object Bold : PartKind
        data class Link(val href: String) : PartKind
    }
}
