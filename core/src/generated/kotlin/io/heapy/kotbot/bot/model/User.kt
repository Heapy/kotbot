package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object represents a Telegram user or bot.
 */
@Serializable
public data class User(
    /**
     * Unique identifier for this user or bot. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a 64-bit integer or double-precision float type are safe for storing this identifier.
     */
    public val id: Long,
    /**
     * *True*, if this user is a bot
     */
    public val is_bot: Boolean,
    /**
     * User's or bot's first name
     */
    public val first_name: String,
    /**
     * *Optional*. User's or bot's last name
     */
    public val last_name: String? = null,
    /**
     * *Optional*. User's or bot's username
     */
    public val username: String? = null,
    /**
     * *Optional*. [IETF language tag](https://en.wikipedia.org/wiki/IETF_language_tag) of the user's language
     */
    public val language_code: String? = null,
    /**
     * *Optional*. *True*, if this user is a Telegram Premium user
     */
    public val is_premium: Boolean? = true,
    /**
     * *Optional*. *True*, if this user added the bot to the attachment menu
     */
    public val added_to_attachment_menu: Boolean? = true,
    /**
     * *Optional*. *True*, if the bot can be invited to groups. Returned only in [getMe](https://core.telegram.org/bots/api/#getme).
     */
    public val can_join_groups: Boolean? = null,
    /**
     * *Optional*. *True*, if [privacy mode](https://core.telegram.org/bots/features#privacy-mode) is disabled for the bot. Returned only in [getMe](https://core.telegram.org/bots/api/#getme).
     */
    public val can_read_all_group_messages: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot supports inline queries. Returned only in [getMe](https://core.telegram.org/bots/api/#getme).
     */
    public val supports_inline_queries: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot can be connected to a Telegram Business account to receive its messages. Returned only in [getMe](https://core.telegram.org/bots/api/#getme).
     */
    public val can_connect_to_business: Boolean? = null,
)
