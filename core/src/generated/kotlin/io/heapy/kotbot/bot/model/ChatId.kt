package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.ChatIdSerializer
import kotlin.Int
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@Serializable(with = ChatIdSerializer::class)
public sealed interface ChatId

@JvmInline
@Serializable
public value class IntChatId(
  public val `value`: Int,
) : ChatId

@JvmInline
@Serializable
public value class StringChatId(
  public val `value`: String,
) : ChatId
