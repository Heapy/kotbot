package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.File
import io.heapy.kotbot.bot.model.InputFile
import kotlin.Long
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to upload a file with a sticker for later use in the [createNewStickerSet](https://core.telegram.org/bots/api/#createnewstickerset) and [addStickerToSet](https://core.telegram.org/bots/api/#addstickertoset) methods (the file can be used multiple times). Returns the uploaded [File](https://core.telegram.org/bots/api/#file) on success.
 */
@Serializable
public data class UploadStickerFile(
    /**
     * User identifier of sticker file owner
     */
    public val user_id: Long,
    /**
     * A file with the sticker in .WEBP, .PNG, .TGS, or .WEBM format. See [https://core.telegram.org/stickers](https://core.telegram.org/stickers) for technical requirements. [More information on Sending Files &raquo;](https://core.telegram.org/bots/api/#sending-files)
     */
    public val sticker: InputFile,
    /**
     * Format of the sticker, must be one of "static", "animated", "video"
     */
    public val sticker_format: String,
) : Method<UploadStickerFile, File> by Companion {
    public companion object : Method<UploadStickerFile, File> {
        override val _deserializer: KSerializer<Response<File>> =
                Response.serializer(File.serializer())

        override val _serializer: KSerializer<UploadStickerFile> = serializer()

        override val _name: String = "uploadStickerFile"
    }
}
