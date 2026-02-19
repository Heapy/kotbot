package io.heapy.kotbot.infra.markdown

import org.commonmark.node.Nodes
import org.commonmark.node.SourceSpans
import org.commonmark.parser.delimiter.DelimiterProcessor
import org.commonmark.parser.delimiter.DelimiterRun

internal class SpoilerDelimiterProcessor : DelimiterProcessor {
    override fun getOpeningCharacter(): Char = '|'

    override fun getClosingCharacter(): Char = '|'

    override fun getMinLength(): Int = 2

    override fun process(openingRun: DelimiterRun, closingRun: DelimiterRun): Int {
        if (openingRun.length() >= 2 && closingRun.length() >= 2) {
            val opener = openingRun.opener
            val delimiter = "${opener.literal}${opener.literal}"

            val spoiler = Spoiler(delimiter)

            val sourceSpans = SourceSpans()
            sourceSpans.addAllFrom(openingRun.getOpeners(2))

            for (node in Nodes.between(opener, closingRun.closer)) {
                spoiler.appendChild(node)
                sourceSpans.addAll(node.sourceSpans)
            }

            sourceSpans.addAllFrom(closingRun.getClosers(2))
            spoiler.sourceSpans = sourceSpans.sourceSpans

            opener.insertAfter(spoiler)

            return 2
        }
        return 0
    }
}
