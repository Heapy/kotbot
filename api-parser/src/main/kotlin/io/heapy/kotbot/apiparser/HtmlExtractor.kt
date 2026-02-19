package io.heapy.kotbot.apiparser

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

private const val BOT_API_DOCS_URL = "https://core.telegram.org/bots/api/"

data class ExtractedApi(
    val recentChangesText: String,
    val versionText: String,
    val methods: List<RawMethod>,
    val objects: List<RawObject>,
)

data class RawMethod(
    val name: String,
    val descriptionElements: List<Element>,
    val arguments: List<RawArgument>,
    val documentationLink: String,
)

data class RawArgument(
    val name: String,
    val type: String,
    val required: String,
    val descriptionElement: Element,
)

data class RawObject(
    val name: String,
    val descriptionElements: List<Element>,
    val data: RawObjectData,
    val documentationLink: String,
)

sealed interface RawObjectData {
    data class Fields(val fields: List<RawField>) : RawObjectData
    data class Elements(val elements: List<String>) : RawObjectData
}

data class RawField(
    val name: String,
    val type: String,
    val descriptionElement: Element,
)

object HtmlExtractor {
    private val RELEVANT_TAGS = setOf("h3", "h4", "p", "table", "ul")

    fun extract(html: String): ExtractedApi {
        val doc = Jsoup.parse(html)
        val content = doc.select("#dev_page_content").first()
            ?: error("No #dev_page_content found")

        // Use select() to match ALL descendants (not just direct children),
        // like the Rust code's select("h3, h4, p, table, ul").
        // This finds p/ul inside blockquotes too.
        val elements = content.select("h3, h4, p, table, ul")

        var recentChanges: String? = null
        var version: String? = null
        val methods = mutableListOf<RawMethod>()
        val objects = mutableListOf<RawObject>()

        var state: State = State.SearchRecentChanges
        var i = 0

        while (i < elements.size) {
            val elem = elements[i]
            val tag = elem.tagName()

            when (state) {
                State.SearchRecentChanges -> {
                    if (tag == "h3" && elem.plainText() == "Recent changes") {
                        state = State.GetRecentChange
                    }
                    i++
                }
                State.GetRecentChange -> {
                    if (tag == "h4") {
                        recentChanges = elem.plainText()
                        state = State.GetVersion
                    }
                    i++
                }
                State.GetVersion -> {
                    if (tag == "p") {
                        version = elem.plainText()
                        state = State.SearchGettingUpdates
                    }
                    i++
                }
                State.SearchGettingUpdates -> {
                    if (tag == "h3" && elem.plainText() == "Getting updates") {
                        state = State.GetName
                    }
                    i++
                }
                State.GetName -> {
                    if (tag == "h4") {
                        val name = elem.plainText()
                        // Skip non-API elements like "Formatting options", "Sending files"
                        if (name.any { it.isWhitespace() }) {
                            i++
                            continue
                        }

                        val docLink = extractDocumentationLink(elem)
                        val nextElem = elements.getOrNull(i + 1)

                        if (nextElem?.tagName() == "table") {
                            // Object/method with table directly after h4 (no description)
                            state = State.GetObjectFields(name, emptyList(), docLink)
                        } else {
                            state = State.GetDescription(name, mutableListOf(), docLink)
                        }
                    }
                    i++
                }
                is State.GetDescription -> {
                    val s = state as State.GetDescription
                    if (tag == "p" || tag == "ul") {
                        s.description.add(elem)
                        i++

                        val nextElem = elements.getOrNull(i)
                        val nextTag = nextElem?.tagName()
                        val isMethod = s.name.first().isLowerCase()

                        val hasNextP = nextTag == "p"
                        val hasNextTable = nextTag == "table"
                        val hasNextUl = nextTag == "ul"

                        if (hasNextP || (hasNextUl && isMethod)) {
                            // Continue collecting description
                        } else {
                            // For objects with a UL as last description + elements UL
                            if (hasNextUl && !isMethod) {
                                s.description.add(nextElem!!)
                            }

                            when {
                                isMethod && hasNextTable -> {
                                    state = State.GetMethodFields(s.name, s.description, s.docLink)
                                }
                                !isMethod && hasNextTable -> {
                                    state = State.GetObjectFields(s.name, s.description, s.docLink)
                                }
                                !isMethod && hasNextUl -> {
                                    state = State.GetObjectElements(s.name, s.description, s.docLink)
                                }
                                isMethod && !hasNextTable -> {
                                    // Method with no arguments
                                    methods.add(RawMethod(
                                        name = s.name,
                                        descriptionElements = s.description,
                                        arguments = emptyList(),
                                        documentationLink = s.docLink,
                                    ))
                                    state = State.GetName
                                }
                                else -> {
                                    // Object with no fields
                                    objects.add(RawObject(
                                        name = s.name,
                                        descriptionElements = s.description,
                                        data = RawObjectData.Fields(emptyList()),
                                        documentationLink = s.docLink,
                                    ))
                                    state = State.GetName
                                }
                            }
                        }
                    } else {
                        i++
                    }
                }
                is State.GetObjectFields -> {
                    val s = state as State.GetObjectFields
                    if (tag == "table") {
                        val fields = extractFields(elem)
                        objects.add(RawObject(
                            name = s.name,
                            descriptionElements = s.description,
                            data = RawObjectData.Fields(fields),
                            documentationLink = s.docLink,
                        ))
                        state = State.GetName
                    }
                    i++
                }
                is State.GetMethodFields -> {
                    val s = state as State.GetMethodFields
                    if (tag == "table") {
                        val args = extractArguments(elem)
                        methods.add(RawMethod(
                            name = s.name,
                            descriptionElements = s.description,
                            arguments = args,
                            documentationLink = s.docLink,
                        ))
                        state = State.GetName
                    }
                    i++
                }
                is State.GetObjectElements -> {
                    val s = state as State.GetObjectElements
                    if (tag == "ul") {
                        val elems = elem.select("li").map { it.plainText() }
                        objects.add(RawObject(
                            name = s.name,
                            descriptionElements = s.description,
                            data = RawObjectData.Elements(elems),
                            documentationLink = s.docLink,
                        ))
                        state = State.GetName
                    }
                    i++
                }
            }
        }

        return ExtractedApi(
            recentChangesText = recentChanges ?: error("No recent changes found"),
            versionText = version ?: error("No version found"),
            methods = methods,
            objects = objects,
        )
    }

    /**
     * Extract plain text from an element, including img alt text and br as newlines.
     * Mirrors the Rust ElementRefExt::plain_text().
     */
    private fun Element.plainText(): String {
        return buildString {
            for (node in childNodes()) {
                appendPlainText(node)
            }
        }
    }

    private fun StringBuilder.appendPlainText(node: org.jsoup.nodes.Node) {
        when (node) {
            is org.jsoup.nodes.TextNode -> append(node.wholeText)
            is Element -> {
                when (node.tagName()) {
                    "img" -> append(node.attr("alt"))
                    "br" -> append("\n")
                    else -> {
                        for (child in node.childNodes()) {
                            appendPlainText(child)
                        }
                    }
                }
            }
        }
    }

    private fun extractDocumentationLink(h4: Element): String {
        val anchor = h4.selectFirst("a")
        val href = anchor?.attr("href") ?: ""
        return makeDocumentationLink(href)
    }

    private fun makeDocumentationLink(href: String): String {
        if (href.startsWith("#")) {
            return "$BOT_API_DOCS_URL$href"
        }
        // Handle Wayback Machine URLs
        val waybackPattern = Regex("/web/\\d+/https://core\\.telegram\\.org/bots/api(#.*)")
        val waybackMatch = waybackPattern.find(href)
        if (waybackMatch != null) {
            return "${BOT_API_DOCS_URL}${waybackMatch.groupValues[1]}"
        }
        return href
    }

    private fun extractFields(table: Element): List<RawField> {
        val tds = table.select("td")
        val fields = mutableListOf<RawField>()
        var i = 0
        while (i + 2 < tds.size) {
            fields.add(RawField(
                name = tds[i].text(),
                type = tds[i + 1].text(),
                descriptionElement = tds[i + 2],
            ))
            i += 3
        }
        return fields
    }

    private fun extractArguments(table: Element): List<RawArgument> {
        val tds = table.select("td")
        val args = mutableListOf<RawArgument>()
        var i = 0
        while (i + 3 < tds.size) {
            args.add(RawArgument(
                name = tds[i].text(),
                type = tds[i + 1].text(),
                required = tds[i + 2].text(),
                descriptionElement = tds[i + 3],
            ))
            i += 4
        }
        return args
    }

    private sealed interface State {
        data object SearchRecentChanges : State
        data object GetRecentChange : State
        data object GetVersion : State
        data object SearchGettingUpdates : State
        data object GetName : State
        data class GetDescription(
            val name: String,
            val description: MutableList<Element>,
            val docLink: String,
        ) : State
        data class GetObjectFields(
            val name: String,
            val description: List<Element>,
            val docLink: String,
        ) : State
        data class GetMethodFields(
            val name: String,
            val description: List<Element>,
            val docLink: String,
        ) : State
        data class GetObjectElements(
            val name: String,
            val description: List<Element>,
            val docLink: String,
        ) : State
    }
}
