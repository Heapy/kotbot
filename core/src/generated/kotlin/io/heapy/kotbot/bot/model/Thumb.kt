package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.ThumbSerializer
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@Serializable(with = ThumbSerializer::class)
public sealed interface Thumb

@JvmInline
@Serializable
public value class InputFileThumb(
    public val `value`: InputFile,
) : Thumb

@JvmInline
@Serializable
public value class StringThumb(
    public val `value`: String,
) : Thumb
