package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlinx.serialization.Serializable

/**
 * Describes actions that a non-administrator user is allowed to take in a chat.
 */
@Serializable
public data class ChatPermissions(
    /**
     * *Optional*. *True*, if the user is allowed to send text messages, contacts, locations and venues
     */
    public val can_send_messages: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to send audios, documents, photos, videos, video notes and voice notes, implies can_send_messages
     */
    public val can_send_media_messages: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to send polls, implies can_send_messages
     */
    public val can_send_polls: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to send animations, games, stickers and use inline bots, implies can_send_media_messages
     */
    public val can_send_other_messages: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to add web page previews to their messages, implies can_send_media_messages
     */
    public val can_add_web_page_previews: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to change the chat title, photo and other settings. Ignored in public supergroups
     */
    public val can_change_info: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to invite new users to the chat
     */
    public val can_invite_users: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to pin messages. Ignored in public supergroups
     */
    public val can_pin_messages: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to create forum topics. If omitted defaults to the value of can_pin_messages
     */
    public val can_manage_topics: Boolean? = null,
)
