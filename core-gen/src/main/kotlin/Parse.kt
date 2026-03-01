@file:JvmName("Parse")

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.io.path.writeText

val Element.level get() = tagName().substring(1).toInt()

fun main() {
    val rootPath = Path("core-gen", "src", "main", "resources")
    listOf(
        "api620",
        "api630",
        "api640",
        "api650",
        "api660",
        "api670",
        "api680",
        "api690",
        "api700",
        "api710",
        "api740",
        "api790",
        "api800",
        "api810",
        "api820",
        "api830",
        "api900",
        "api920",
        "api940",
        "api950",
    ).forEach { v ->
        val input = rootPath.resolve(v).readText()
        val output = processVersion(input)
        rootPath.resolve("$v.md").writeText(output)
    }
}

fun processVersion(input: String): String {
    val children = Jsoup.parse(input)
        .select("#dev_page_content")
        .first()
        ?.children()
        ?: error("No content found")

    return buildString {
        children.forEach { el ->
            appendLine()
            when (el.tagName()) {
                "h1" -> appendLine("# ${el.text()}")
                "h2" -> appendLine("## ${el.text()}")
                "h3" -> appendLine("### ${el.text()}")
                "h4" -> appendLine("#### ${el.text()}")
                "h5" -> appendLine("##### ${el.text()}")
                "h6" -> appendLine("###### ${el.text()}")
                "p" -> appendLine(el.text())
                "ul" -> {
                    el.children().forEach { li ->
                        appendLine("* ${li.text()}")
                    }
                }

                "ol" -> {
                    el.children().forEach { li ->
                        appendLine("1. ${li.text()}")
                    }
                }

                "pre" -> {
                    appendLine(el.text())
                }

                "table" -> {
                    appendLine("| ${el.select("th").joinToString(" | ") { it.text() }} |")
                    appendLine("| ${el.select("th").joinToString(" | ") { "---" }} |")
                    el.select("tr").forEach { tr ->
                        appendLine("| ${tr.select("td").joinToString(" | ") { it.text() }} |")
                    }
                }

                "blockquote" -> {
                    appendLine("> ${el.text()}")
                }

                "hr" -> {
                    appendLine("---")
                }

                else -> {
                    println("Unknown tag: ${el.tagName()}")
                }
            }
            appendLine()
        }
    }
}
