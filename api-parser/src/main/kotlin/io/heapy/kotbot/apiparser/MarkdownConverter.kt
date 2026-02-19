package io.heapy.kotbot.apiparser

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.jsoup.parser.Parser

private const val CORE_TELEGRAM_URL = "https://core.telegram.org"
private const val BOT_API_DOCS_URL = "https://core.telegram.org/bots/api/"

/**
 * Converts HTML to markdown, matching the output of the Rust html2md library
 * with custom tag handlers for anchors and images.
 */
object MarkdownConverter {
    /**
     * Convert HTML string (potentially multiple elements joined with \n) to markdown.
     */
    fun convert(html: String): String {
        // Parse without pretty-printing to avoid Jsoup adding whitespace
        val doc = Jsoup.parse(html, "", Parser.htmlParser())
        doc.outputSettings().indentAmount(0).outline(false)
        val body = doc.body()
        val raw = renderNode(body)
        return postProcess(raw)
    }

    /**
     * Convert a single element to markdown.
     */
    fun convertElement(element: Element): String {
        val raw = renderNode(element)
        return postProcess(raw)
    }

    /**
     * Post-process rendered markdown to match html2md output:
     * - Clean up whitespace around line breaks and paragraph breaks
     * - Trim trailing whitespace
     */
    private fun postProcess(raw: String): String {
        var result = raw

        // 1. Clean up spaces around <br>-generated "  \n":
        //    "text.   \n text" → "text.  \ntext" (preserve exactly 2 trailing spaces)
        result = result.replace(Regex(" {3,}\\n"), "  \n")

        // 2. Remove leading spaces after newlines (from HTML indentation)
        result = result.replace(Regex("\\n +"), "\n")

        // 3. Collapse 3+ newlines to exactly 2
        result = result.replace(Regex("\\n{3,}"), "\n\n")

        // 4. Escape dashes at start of lines (could be interpreted as markdown lists)
        result = result.replace(Regex("(?<=\\n)- "), Regex.escapeReplacement("\\- "))

        return result.trimEnd()
    }

    private fun renderNode(node: Node): String {
        return when (node) {
            is TextNode -> {
                // Collapse whitespace like HTML rendering does:
                // multiple spaces/newlines/tabs → single space
                val collapsed = node.wholeText.replace(Regex("[\\s]+"), " ")
                escapeMarkdown(collapsed)
            }
            is Element -> renderElement(node)
            else -> ""
        }
    }

    private fun renderChildren(parent: Node): String {
        return buildString {
            for (child in parent.childNodes()) {
                append(renderNode(child))
            }
        }
    }

    /**
     * Escape markdown special characters in text nodes.
     * The Rust html2md escapes underscores, angle brackets, and asterisks.
     */
    private fun escapeMarkdown(text: String): String {
        return text.replace("_", "\\_")
            .replace("*", "\\*")
            .replace(">", "\\>")
            .replace("<", "\\<")
    }

    private fun renderElement(element: Element): String {
        val tag = element.tagName()
        return when (tag) {
            "body" -> renderChildren(element)
            "p" -> renderChildren(element) + "\n\n"
            "br" -> "  \n"
            "strong", "b" -> "**${renderChildren(element)}**"
            "em", "i" -> "*${renderChildren(element)}*"
            "code" -> "`${renderChildrenNoEscape(element)}`"
            "pre" -> "```\n${renderChildrenNoEscape(element)}\n```"
            "a" -> renderAnchor(element)
            "img" -> renderImage(element)
            "ul" -> renderUnorderedList(element)
            "ol" -> renderOrderedList(element)
            "li" -> renderChildren(element)
            "blockquote" -> renderChildren(element)
            "td", "th" -> renderChildren(element)
            "table", "thead", "tbody", "tr" -> renderChildren(element)
            "h1", "h2", "h3", "h4", "h5", "h6" -> renderChildren(element)
            "div", "span" -> renderChildren(element)
            else -> renderChildren(element)
        }
    }

    /**
     * Render children without any escaping (for code blocks).
     */
    private fun renderChildrenNoEscape(parent: Node): String {
        return buildString {
            for (child in parent.childNodes()) {
                when (child) {
                    is TextNode -> append(child.wholeText)
                    is Element -> {
                        if (child.tagName() == "br") append("\n")
                        else append(renderChildrenNoEscape(child))
                    }
                    else -> {}
                }
            }
        }
    }

    private fun renderAnchor(element: Element): String {
        val href = element.attr("href")
        val text = renderChildren(element)

        if (text.isEmpty()) return ""

        val resolvedHref = resolveUrl(href)
        return "[$text]($resolvedHref)"
    }

    private fun renderImage(element: Element): String {
        val alt = element.attr("alt")
        return if (alt.isNotEmpty()) {
            alt
        } else {
            element.outerHtml()
        }
    }

    private fun renderUnorderedList(element: Element): String {
        return buildString {
            append("\n")
            for (li in element.children()) {
                if (li.tagName() == "li") {
                    append("* ${renderChildren(li).trim()}\n")
                }
            }
        }
    }

    private fun renderOrderedList(element: Element): String {
        return buildString {
            append("\n")
            var idx = 1
            for (li in element.children()) {
                if (li.tagName() == "li") {
                    append("${idx}. ${renderChildren(li).trim()}\n")
                    idx++
                }
            }
        }
    }

    fun resolveUrl(href: String): String {
        if (href.isEmpty()) return href

        // Strip Wayback Machine prefix: /web/TIMESTAMP/https://...
        val waybackPattern = Regex("/web/\\d+/(https?://.*)")
        val waybackMatch = waybackPattern.find(href)
        if (waybackMatch != null) {
            return waybackMatch.groupValues[1]
        }

        // Wayback: https://web.archive.org/web/TIMESTAMP/https://...
        val waybackPattern2 = Regex("https://web\\.archive\\.org/web/\\d+/(https?://.*)")
        val waybackMatch2 = waybackPattern2.find(href)
        if (waybackMatch2 != null) {
            return waybackMatch2.groupValues[1]
        }

        // Wayback with im_: /web/TIMESTAMPim_/https://...
        val waybackPattern3 = Regex("/web/\\d+im_/(https?://.*)")
        val waybackMatch3 = waybackPattern3.find(href)
        if (waybackMatch3 != null) {
            return waybackMatch3.groupValues[1]
        }

        // Fragment-only links
        if (href.startsWith("#")) {
            return "$BOT_API_DOCS_URL$href"
        }

        // Relative links (not protocol-relative)
        if (href.startsWith("/") && !href.startsWith("//")) {
            return "$CORE_TELEGRAM_URL$href"
        }

        // Protocol-relative
        if (href.startsWith("//")) {
            return "https:$href"
        }

        return href
    }
}
