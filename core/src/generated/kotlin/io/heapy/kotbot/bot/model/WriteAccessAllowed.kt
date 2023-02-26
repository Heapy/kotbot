package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object represents a service message about a user allowing a bot to write messages after adding the bot to the attachment menu or launching a Web App from a link.
 */
@Serializable
public data class WriteAccessAllowed(
    /**
     * *Optional*. Name of the Web App which was launched from a link
     */
    public val web_app_name: String? = null,
)
