package io.heapy.tgpt.infra.markdown

import org.commonmark.node.CustomNode
import org.commonmark.node.Delimited

class Spoiler(
    private val delimiter: String,
) : CustomNode(), Delimited {
    override fun getOpeningDelimiter(): String = delimiter
    override fun getClosingDelimiter(): String = delimiter
}
