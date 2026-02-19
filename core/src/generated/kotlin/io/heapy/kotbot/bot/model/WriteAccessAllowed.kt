package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object represents a service message about a user allowing a bot to write messages after adding it to the attachment menu, launching a Web App from a link, or accepting an explicit request from a Web App sent by the method [requestWriteAccess](https://core.telegram.org/bots/webapps#initializing-mini-apps).
 */
@Serializable
public data class WriteAccessAllowed(
    /**
     * *Optional*. *True*, if the access was granted after the user accepted an explicit request from a Web App sent by the method [requestWriteAccess](https://core.telegram.org/bots/webapps#initializing-mini-apps)
     */
    public val from_request: Boolean? = null,
    /**
     * *Optional*. Name of the Web App, if the access was granted when the Web App was launched from a link
     */
    public val web_app_name: String? = null,
    /**
     * *Optional*. *True*, if the access was granted when the bot was added to the attachment or side menu
     */
    public val from_attachment_menu: Boolean? = null,
)
