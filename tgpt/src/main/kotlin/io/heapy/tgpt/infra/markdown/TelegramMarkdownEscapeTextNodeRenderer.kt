package io.heapy.tgpt.infra.markdown

import org.commonmark.node.BulletList
import org.commonmark.node.Emphasis
import org.commonmark.node.Heading
import org.commonmark.node.ListItem
import org.commonmark.node.Node
import org.commonmark.node.OrderedList
import org.commonmark.node.StrongEmphasis
import org.commonmark.renderer.markdown.CoreMarkdownNodeRenderer
import org.commonmark.renderer.markdown.MarkdownNodeRendererContext

class TelegramMarkdownEscapeTextNodeRenderer(
    private val rendererContext: MarkdownNodeRendererContext,
) : CoreMarkdownNodeRenderer(rendererContext) {

    override fun getNodeTypes(): Set<Class<out Node>> {
        return setOf(
            OrderedList::class.java,
            BulletList::class.java,
            ListItem::class.java,
            Emphasis::class.java,
            StrongEmphasis::class.java,
            Heading::class.java,
            Spoiler::class.java,
        )
    }

    private open class ListHolder(
        val parent: ListHolder?,
    )

    private class BulletListHolder(
        parent: ListHolder?,
        bulletList: BulletList,
    ) : ListHolder(parent) {
        val marker: String = bulletList.marker ?: "-"
    }

    private class OrderedListHolder(
        parent: ListHolder?,
        orderedList: OrderedList,
    ) : ListHolder(parent) {
        val delimiter: String = orderedList.markerDelimiter ?: "\\."
        var number: Int = orderedList.markerStartNumber ?: 1
    }

    /**
     * If we're currently within a [BulletList] or [OrderedList], this keeps the context of that list.
     * It has a parent field so that it can represent a stack (for nested lists).
     */
    private var listHolder: ListHolder? = null

    override fun visit(bulletList: BulletList) {
        val writer = rendererContext.writer
        writer.pushTight(bulletList.isTight)
        listHolder = BulletListHolder(listHolder, bulletList)
        visitChildren(bulletList)
        listHolder = listHolder?.parent
        writer.popTight()
        writer.block()
    }

    override fun visit(orderedList: OrderedList) {
        val writer = rendererContext.writer
        writer.pushTight(orderedList.isTight)
        listHolder = OrderedListHolder(listHolder, orderedList)
        visitChildren(orderedList)
        listHolder = listHolder?.parent
        writer.popTight()
        writer.block()
    }

    override fun visit(heading: Heading) {
        val writer = rendererContext.writer
        writer.raw("*")
        visitChildren(heading)
        writer.raw("*")
        writer.block()
    }

    override fun visit(emphasis: Emphasis) {
        val writer = rendererContext.writer
        writer.raw("_")
        visitChildren(emphasis)
        writer.raw("_")
    }

    override fun visit(strongEmphasis: StrongEmphasis) {
        val writer = rendererContext.writer
        writer.raw("*")
        visitChildren(strongEmphasis)
        writer.raw("*")
    }

    override fun visit(listItem: ListItem) {
        val writer = rendererContext.writer
        val markerIndent = listItem.markerIndent ?: 0
        val marker: String
        when (val actualList = listHolder) {
            is BulletListHolder -> {
                marker = " ".repeat(markerIndent) + escapePlainTextForMarkdownV2(actualList.marker)
            }

            is OrderedListHolder -> {
                marker = " ".repeat(markerIndent) + actualList.number + escapePlainTextForMarkdownV2(actualList.delimiter)
                actualList.number++
            }

            else -> {
                throw IllegalStateException("Unknown list holder type: $listHolder")
            }
        }
        val contentIndent = listItem.contentIndent
        val spaces = if (contentIndent != null) " ".repeat(contentIndent - marker.length + 1) else " "
        writer.writePrefix(marker)
        writer.writePrefix(spaces)
        writer.pushPrefix(" ".repeat(marker.length + spaces.length))

        if (listItem.firstChild == null) {
            // Empty list item
            writer.block()
        } else {
            visitChildren(listItem)
        }

        writer.popPrefix()
    }

    override fun render(node: Node) {
        if (node is Spoiler) {
            val writer = rendererContext.writer
            writer.raw("||")
            visitChildren(node)
            writer.raw("||")
        } else {
            node.accept(this)
        }
    }

    companion object {
        val specialCharacters = "`*[]()_~>#+-=|{}.!".toCharArray()
    }
}
