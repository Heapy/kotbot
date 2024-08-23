package io.heapy.kotbot.bot.model

public val Long.chatId: ChatId get() = LongChatId(this)
public val String.chatId: ChatId get() = StringChatId(this)
