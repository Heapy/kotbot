package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.File
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to get basic information about a file and prepare it for downloading. For the moment, bots can download files of up to 20MB in size. On success, a [File](https://core.telegram.org/bots/api/#file) object is returned. The file can then be downloaded via the link `https://api.telegram.org/file/bot<token>/<file_path>`, where `<file_path>` is taken from the response. It is guaranteed that the link will be valid for at least 1 hour. When the link expires, a new one can be requested by calling [getFile](https://core.telegram.org/bots/api/#getfile) again.
 */
@Serializable
public data class GetFile(
    /**
     * File identifier to get information about
     */
    public val file_id: String,
) : Method<GetFile, File> by Companion {
    public companion object : Method<GetFile, File> {
        override val _deserializer: KSerializer<Response<File>> =
                Response.serializer(File.serializer())

        override val _serializer: KSerializer<GetFile> = serializer()

        override val _name: String = "getFile"
    }
}
