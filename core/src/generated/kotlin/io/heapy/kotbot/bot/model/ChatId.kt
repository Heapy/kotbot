package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.ChatIdSerializer
import kotlin.Long
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@Serializable(with = ChatIdSerializer::class)
public sealed interface ChatId

@JvmInline
@Serializable
public value class LongChatId(
  public val `value`: Long,
) : ChatId

@JvmInline
@Serializable
public value class StringChatId(
  public val `value`: String,
) : ChatId
