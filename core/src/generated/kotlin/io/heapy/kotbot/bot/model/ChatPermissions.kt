package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlinx.serialization.Serializable

/**
 * Describes actions that a non-administrator user is allowed to take in a chat.
 */
@Serializable
public data class ChatPermissions(
    /**
     * *Optional*. *True*, if the user is allowed to send text messages, contacts, invoices, locations and venues
     */
    public val can_send_messages: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to send audios
     */
    public val can_send_audios: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to send documents
     */
    public val can_send_documents: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to send photos
     */
    public val can_send_photos: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to send videos
     */
    public val can_send_videos: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to send video notes
     */
    public val can_send_video_notes: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to send voice notes
     */
    public val can_send_voice_notes: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to send polls
     */
    public val can_send_polls: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to send animations, games, stickers and use inline bots
     */
    public val can_send_other_messages: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to add web page previews to their messages
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
