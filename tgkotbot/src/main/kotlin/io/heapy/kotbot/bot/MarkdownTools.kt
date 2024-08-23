package io.heapy.kotbot.bot

fun escapeMarkdownV2(text: String): String {
    val escapeChars = "_[]()~>#+-=|{}.!".toCharArray()
    val escapedText = StringBuilder()

    for (char in text) {
        if (char in escapeChars) {
            escapedText.append("\\")
        }
        escapedText.append(char)
    }

    return escapedText.toString()
}
