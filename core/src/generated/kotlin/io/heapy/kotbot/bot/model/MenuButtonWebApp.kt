package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents a menu button, which launches a [Web App](https://core.telegram.org/bots/webapps).
 */
@Serializable
public data class MenuButtonWebApp(
  /**
   * Type of the button, must be *web_app*
   */
  public val type: String = "web_app",
  /**
   * Text on the button
   */
  public val text: String,
  /**
   * Description of the Web App that will be launched when the user presses the button. The Web App will be able to send an arbitrary message on behalf of the user using the method [answerWebAppQuery](https://core.telegram.org/bots/api/#answerwebappquery).
   */
  public val web_app: WebAppInfo,
) : MenuButton
