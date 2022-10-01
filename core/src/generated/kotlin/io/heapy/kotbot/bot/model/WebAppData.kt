package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Describes data sent from a [Web App](https://core.telegram.org/bots/webapps) to the bot.
 */
@Serializable
public data class WebAppData(
  /**
   * The data. Be aware that a bad client can send arbitrary data in this field.
   */
  public val `data`: String,
  /**
   * Text of the *web_app* keyboard button from which the Web App was opened. Be aware that a bad client can send arbitrary data in this field.
   */
  public val button_text: String,
)
