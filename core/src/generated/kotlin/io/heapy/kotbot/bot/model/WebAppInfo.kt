package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Describes a [Web App](https://core.telegram.org/bots/webapps).
 */
@Serializable
public data class WebAppInfo(
  /**
   * An HTTPS URL of a Web App to be opened with additional data as specified in [Initializing Web Apps](https://core.telegram.org/bots/webapps#initializing-web-apps)
   */
  public val url: String,
)
