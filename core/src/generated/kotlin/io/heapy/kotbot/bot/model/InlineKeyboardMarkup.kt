package io.heapy.kotbot.bot.model

import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * This object represents an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) that appears right next to the message it belongs to.
 */
@Serializable
public data class InlineKeyboardMarkup(
  /**
   * Array of button rows, each represented by an Array of [InlineKeyboardButton](https://core.telegram.org/bots/api/#inlinekeyboardbutton) objects
   */
  public val inline_keyboard: List<List<InlineKeyboardButton>>,
)
