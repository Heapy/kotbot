package io.heapy.kotbot.infra.markdown

import org.commonmark.renderer.NodeRenderer
import org.commonmark.renderer.markdown.MarkdownNodeRendererContext
import org.commonmark.renderer.markdown.MarkdownNodeRendererFactory

internal class TelegramMarkdownEscapeTextVisitorFactory : MarkdownNodeRendererFactory {
    override fun create(context: MarkdownNodeRendererContext): NodeRenderer {
        return TelegramMarkdownEscapeTextNodeRenderer(context)
    }

    override fun getSpecialCharacters(): Set<Char> {
        return TelegramMarkdownEscapeTextNodeRenderer.specialCharacters.toSet()
    }
}
