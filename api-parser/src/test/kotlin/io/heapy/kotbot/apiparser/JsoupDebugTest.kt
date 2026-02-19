package io.heapy.kotbot.apiparser

import org.jsoup.Jsoup
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser
import org.junit.jupiter.api.Test

class JsoupDebugTest {
    @Test
    fun `debug html rendering`() {
        // Load the actual api830 HTML
        val fullHtml = this::class.java.getResource("/api830")!!.readText()
        val doc = Jsoup.parse(fullHtml)
        val content = doc.select("#dev_page_content").first()!!

        // Find the td with "allowed_updates" for getUpdates (first occurrence)
        val tds = content.select("td")
        for (i in tds.indices) {
            if (tds[i].text() == "allowed_updates") {
                // Description is 2 more tds ahead in a 4-column table
                val descTd = tds[i + 3]
                println("=== allowed_updates description td ===")
                println("outerHtml: ${descTd.outerHtml().take(300)}")
                println()
                println("=== Converting with MarkdownConverter ===")
                val md = MarkdownConverter.convertElement(descTd)
                println("Result: ${md.take(300)}")
                println()

                // Check raw child nodes
                println("=== Child nodes ===")
                for (node in descTd.childNodes()) {
                    when (node) {
                        is TextNode -> println("TEXT: '${node.wholeText.take(80)}'")
                        is Element -> println("ELEM: <${node.tagName()}> text='${node.text().take(50)}'")
                    }
                }
                break
            }
        }

        // Also check the Update object description
        val ps = content.select("p")
        for (p in ps) {
            if (p.text().startsWith("This object represents an incoming update.")) {
                println("\n=== Update description p ===")
                println("outerHtml: ${p.outerHtml().take(400)}")
                println()
                println("=== Child nodes ===")
                for (node in p.childNodes()) {
                    when (node) {
                        is TextNode -> println("TEXT: '${node.wholeText.take(80)}'")
                        is Element -> println("ELEM: <${node.tagName()}> text='${node.text().take(50)}'")
                    }
                }
                break
            }
        }
    }
}
