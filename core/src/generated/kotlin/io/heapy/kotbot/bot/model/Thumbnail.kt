package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.ThumbnailSerializer
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@Serializable(with = ThumbnailSerializer::class)
public sealed interface Thumbnail

@JvmInline
@Serializable
public value class InputFileThumbnail(
    public val `value`: InputFile,
) : Thumbnail

@JvmInline
@Serializable
public value class StringThumbnail(
    public val `value`: String,
) : Thumbnail
