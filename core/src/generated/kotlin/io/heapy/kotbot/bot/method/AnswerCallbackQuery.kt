package io.heapy.kotbot.bot.method

import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Use this method to send answers to callback queries sent from [inline keyboards](https://core.telegram.org/bots#inline-keyboards-and-on-the-fly-updating). The answer will be displayed to the user as a notification at the top of the chat screen or as an alert. On success, *True* is returned.
 *
 * Alternatively, the user can be redirected to the specified Game URL. For this option to work, you must first create a game for your bot via [@BotFather](https://t.me/botfather) and accept the terms. Otherwise, you may use links like `t.me/your_bot?start=XXXX` that open your bot with a parameter.
 */
@Serializable
public data class AnswerCallbackQuery(
  /**
   * Unique identifier for the query to be answered
   */
  public val callback_query_id: String,
  /**
   * Text of the notification. If not specified, nothing will be shown to the user, 0-200 characters
   */
  public val text: String? = null,
  /**
   * If *True*, an alert will be shown by the client instead of a notification at the top of the chat screen. Defaults to *false*.
   */
  public val show_alert: Boolean? = false,
  /**
   * URL that will be opened by the user's client. If you have created a [Game](https://core.telegram.org/bots/api/#game) and accepted the conditions via [@BotFather](https://t.me/botfather), specify the URL that opens your game - note that this will only work if the query comes from a [*callback_game*](https://core.telegram.org/bots/api/#inlinekeyboardbutton) button.  
   *
   * Otherwise, you may use links like `t.me/your_bot?start=XXXX` that open your bot with a parameter.
   */
  public val url: String? = null,
  /**
   * The maximum amount of time in seconds that the result of the callback query may be cached client-side. Telegram apps will support caching starting in version 3.14. Defaults to 0.
   */
  public val cache_time: Int? = 0,
)
