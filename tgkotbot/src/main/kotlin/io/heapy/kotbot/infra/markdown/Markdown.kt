package io.heapy.kotbot.infra.markdown

import org.commonmark.parser.Parser
import org.commonmark.renderer.Renderer

class Markdown internal constructor(
    private val parser: Parser,
    private val renderer: Renderer,
) {
    fun escape(text: String): String {
        val document = parser.parse(text)
        return renderer.render(document)
    }
}
