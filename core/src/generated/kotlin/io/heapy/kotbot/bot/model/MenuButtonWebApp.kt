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
    public val type: String,
    /**
     * Text on the button
     */
    public val text: String,
    /**
     * Description of the Web App that will be launched when the user presses the button. The Web App will be able to send an arbitrary message on behalf of the user using the method [answerWebAppQuery](https://core.telegram.org/bots/api/#answerwebappquery). Alternatively, a `t.me` link to a Web App of the bot can be specified in the object instead of the Web App's URL, in which case the Web App will be opened as if the user pressed the link.
     */
    public val web_app: WebAppInfo,
) : MenuButton
