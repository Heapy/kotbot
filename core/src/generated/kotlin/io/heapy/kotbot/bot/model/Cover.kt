package io.heapy.kotbot.bot.model

import kotlinx.serialization.Serializable

@Serializable
public sealed interface Cover

@JvmInline
@Serializable
public value class FileId(
    public val value: String
) : Cover

@JvmInline
@Serializable
public value class FileUrl(
    public val value: String
) : Cover

@JvmInline
@Serializable
public value class FileAttachment(
    public val value: String
) : Cover
