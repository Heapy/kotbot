package io.heapy.tgpt.infra.markdown

import io.heapy.komok.tech.di.lib.Module
import org.commonmark.parser.Parser
import org.commonmark.renderer.NodeRenderer
import org.commonmark.renderer.Renderer
import org.commonmark.renderer.markdown.CoreMarkdownNodeRenderer
import org.commonmark.renderer.markdown.MarkdownNodeRendererContext
import org.commonmark.renderer.markdown.MarkdownNodeRendererFactory

@Module
open class MarkdownModule {
    open val parser: Parser by lazy {
        Parser.builder()
            .customDelimiterProcessor(SpoilerDelimiterProcessor())
            .build()
    }

    open val telegramMarkdownEscapeTextVisitorFactory by lazy {
        TelegramMarkdownEscapeTextVisitorFactory()
    }

    open val renderer: Renderer by lazy {
        TelegramMarkdownRenderer(
            nodeRendererFactories = listOf(
                telegramMarkdownEscapeTextVisitorFactory,
                object : MarkdownNodeRendererFactory {
                    override fun create(context: MarkdownNodeRendererContext): NodeRenderer {
                        return CoreMarkdownNodeRenderer(context)
                    }

                    override fun getSpecialCharacters(): Set<Char> {
                        return setOf()
                    }
                }
            )
        )
    }

    open val markdown by lazy {
        Markdown(
            parser = parser,
            renderer = renderer,
        )
    }
}
