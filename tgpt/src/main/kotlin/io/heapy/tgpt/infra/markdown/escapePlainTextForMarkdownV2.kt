package io.heapy.tgpt.infra.markdown

import io.heapy.tgpt.infra.markdown.TelegramMarkdownEscapeTextNodeRenderer.Companion.specialCharacters

internal fun escapePlainTextForMarkdownV2(text: String): String {
    val escapedText = StringBuilder()

    for (char in text) {
        if (char in specialCharacters) {
            escapedText.append('\\')
        }
        escapedText.append(char)
    }

    return escapedText.toString()
}
