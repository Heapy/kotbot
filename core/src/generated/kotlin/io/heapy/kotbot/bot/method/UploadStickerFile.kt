package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.model.InputFile
import kotlin.Long
import kotlinx.serialization.Serializable

/**
 * Use this method to upload a .PNG file with a sticker for later use in *createNewStickerSet* and *addStickerToSet* methods (can be used multiple times). Returns the uploaded [File](https://core.telegram.org/bots/api/#file) on success.
 */
@Serializable
public data class UploadStickerFile(
  /**
   * User identifier of sticker file owner
   */
  public val user_id: Long,
  /**
   * **PNG** image with the sticker, must be up to 512 kilobytes in size, dimensions must not exceed 512px, and either width or height must be exactly 512px. [More information on Sending Files &raquo;](https://core.telegram.org/bots/api/#sending-files)
   */
  public val png_sticker: InputFile,
)
