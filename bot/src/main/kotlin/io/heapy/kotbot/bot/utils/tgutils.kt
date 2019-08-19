package io.heapy.kotbot.bot.utils

import org.telegram.telegrambots.meta.api.objects.*

private val T_ME = "https://t.me/"

val Chat.publicLink: String?
    get() = userName?.let { "$T_ME$it" }

val Message.publicLink: String?
    get() = if(!chat.isChannelChat && !chat.isSuperGroupChat) null else chat.userName?.let { "$T_ME$it/$messageId" }

val User.publicLink: String?
    get() = userName?.let { "$T_ME$it" }

val User.fullName: String
    get() = "$firstName $lastName"

val User.fullRef: String
    get() = userName?.let { "$fullName (@$it)" } ?: fullName
