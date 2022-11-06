package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.MessageOrTrueSerializer
import kotlin.Boolean
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@Serializable(with = MessageOrTrueSerializer::class)
public sealed interface MessageOrTrue

@JvmInline
@Serializable
public value class MessageValue(
    public val `value`: Message,
) : MessageOrTrue

@JvmInline
@Serializable
public value class BooleanValue(
    public val `value`: Boolean,
) : MessageOrTrue
