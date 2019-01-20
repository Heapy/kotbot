package io.heapy.kotbot.bot

/**
 * List of stop words.
 *
 * @author Ruslan Ibragimov
 * @since 1.0.0
 */
internal val stopList = listOf(
    // Please Don't Say Just Hello In Chat
    "hi",
    "hello",
    "привет",

    // offensive bydlo style
    "посан",
    "пацан",
    "посоны",
    "пацаны",
    "пасаны",
    "посаны",
    "посоны",

    // swearing
    "блэт",
    "блэд",
    "хуй",
    "хуя",
    "хуёвый",
    "хуйня",
    "хуем",
    "охуеть",
    "пизда",
    "сука",
    "блять",
    "ебать",
    "ебло",
    "ёб"
)
