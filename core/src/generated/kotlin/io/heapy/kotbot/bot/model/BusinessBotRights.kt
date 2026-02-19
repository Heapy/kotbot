package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlinx.serialization.Serializable

/**
 * Represents the rights of a business bot.
 */
@Serializable
public data class BusinessBotRights(
    /**
     * *Optional*. *True*, if the bot can send and edit messages in the private chats that had incoming messages in the last 24 hours
     */
    public val can_reply: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot can mark incoming private messages as read
     */
    public val can_read_messages: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot can delete messages sent by the bot
     */
    public val can_delete_sent_messages: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot can delete all private messages in managed chats
     */
    public val can_delete_all_messages: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot can edit the first and last name of the business account
     */
    public val can_edit_name: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot can edit the bio of the business account
     */
    public val can_edit_bio: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot can edit the profile photo of the business account
     */
    public val can_edit_profile_photo: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot can edit the username of the business account
     */
    public val can_edit_username: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot can change the privacy settings pertaining to gifts for the business account
     */
    public val can_change_gift_settings: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot can view gifts and the amount of Telegram Stars owned by the business account
     */
    public val can_view_gifts_and_stars: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot can convert regular gifts owned by the business account to Telegram Stars
     */
    public val can_convert_gifts_to_stars: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot can transfer and upgrade gifts owned by the business account
     */
    public val can_transfer_and_upgrade_gifts: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot can transfer Telegram Stars received by the business account to its own account, or use them to upgrade and transfer gifts
     */
    public val can_transfer_stars: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot can post, edit and delete stories on behalf of the business account
     */
    public val can_manage_stories: Boolean? = null,
)
