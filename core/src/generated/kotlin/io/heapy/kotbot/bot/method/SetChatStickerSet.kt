package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.model.ChatId
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Use this method to set a new group sticker set for a supergroup. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Use the field *can_set_sticker_set* optionally returned in [getChat](https://core.telegram.org/bots/api/#getchat) requests to check if the bot can use this method. Returns *True* on success.
 */
@Serializable
public data class SetChatStickerSet(
  /**
   * Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
   */
  public val chat_id: ChatId,
  /**
   * Name of the sticker set to be set as the group sticker set
   */
  public val sticker_set_name: String,
)
