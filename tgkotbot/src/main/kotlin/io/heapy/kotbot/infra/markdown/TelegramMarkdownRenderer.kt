package io.heapy.kotbot.infra.markdown

import org.commonmark.internal.renderer.NodeRendererMap
import org.commonmark.node.Node
import org.commonmark.renderer.Renderer
import org.commonmark.renderer.markdown.MarkdownNodeRendererContext
import org.commonmark.renderer.markdown.MarkdownNodeRendererFactory
import org.commonmark.renderer.markdown.MarkdownWriter
import java.util.Collections

class TelegramMarkdownRenderer(
    private val nodeRendererFactories: List<MarkdownNodeRendererFactory>,
) : Renderer {
    override fun render(node: Node, output: Appendable) {
        val context = RendererContext(
            writer = MarkdownWriter(output),
            nodeRendererFactories = nodeRendererFactories,
        )
        context.render(node)
    }

    override fun render(node: Node): String {
        val sb = StringBuilder()
        render(node, sb)
        return sb.toString()
    }

    private class RendererContext(
        private val writer: MarkdownWriter,
        nodeRendererFactories: List<MarkdownNodeRendererFactory>,
    ) : MarkdownNodeRendererContext {
        private val nodeRendererMap = NodeRendererMap()
        private val additionalTextEscapes: MutableSet<Char>

        init {
            // Set fields that are used by interface
            val escapes = mutableSetOf<Char>()
            for (factory in nodeRendererFactories) {
                escapes.addAll(factory.specialCharacters)
            }
            additionalTextEscapes = Collections.unmodifiableSet<Char>(escapes)

            // The first node renderer for a node type "wins".
            for (i in nodeRendererFactories.indices) {
                val nodeRendererFactory: MarkdownNodeRendererFactory = nodeRendererFactories.get(i)
                // Pass in this as context here, which uses the fields set above
                val nodeRenderer = nodeRendererFactory.create(this)
                nodeRendererMap.add(nodeRenderer)
            }
        }

        override fun getWriter(): MarkdownWriter {
            return writer
        }

        override fun render(node: Node) {
            nodeRendererMap.render(node)
        }

        override fun getSpecialCharacters(): MutableSet<Char> {
            return additionalTextEscapes
        }
    }
}
