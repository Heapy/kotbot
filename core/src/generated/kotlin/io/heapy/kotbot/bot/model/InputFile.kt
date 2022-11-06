package io.heapy.kotbot.bot.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * This object represents the contents of a file to be uploaded. Must be posted using multipart/form-data in the usual way that files are uploaded via the browser.
 */
@Serializable
@JvmInline
public value class InputFile(
    public val `value`: String,
)
