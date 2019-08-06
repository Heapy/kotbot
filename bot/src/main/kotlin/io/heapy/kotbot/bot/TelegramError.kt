package io.heapy.kotbot.bot

enum class TelegramError(val code: Int) {
    BadRequest(400);

    companion object {
        private val values = values()

        fun byCode(code: Int): TelegramError? = values.firstOrNull { it.code == code }
    }
}
