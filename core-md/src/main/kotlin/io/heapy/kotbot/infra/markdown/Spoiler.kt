package io.heapy.kotbot.infra.markdown

import org.commonmark.node.CustomNode
import org.commonmark.node.Delimited

internal class Spoiler(
    private val delimiter: String,
) : CustomNode(), Delimited {
    override fun getOpeningDelimiter(): String = delimiter
    override fun getClosingDelimiter(): String = delimiter
}
