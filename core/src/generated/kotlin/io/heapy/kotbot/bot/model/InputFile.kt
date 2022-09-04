package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.EmptyObject
import kotlinx.serialization.Serializable

/**
 * This object represents the contents of a file to be uploaded. Must be posted using multipart/form-data in the usual way that files are uploaded via the browser.
 */
@Serializable
@EmptyObject
public class InputFile
