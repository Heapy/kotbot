package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Describes a unique gift received and owned by a user or a chat.
 */
@Serializable
public data class OwnedGiftUnique(
    /**
     * Type of the gift, always "unique"
     */
    public val type: String,
    /**
     * Information about the unique gift
     */
    public val gift: UniqueGift,
    /**
     * *Optional*. Unique identifier of the received gift for the bot; for gifts received on behalf of business accounts only
     */
    public val owned_gift_id: String? = null,
    /**
     * *Optional*. Sender of the gift if it is a known user
     */
    public val sender_user: User? = null,
    /**
     * Date the gift was sent in Unix time
     */
    public val send_date: Long,
    /**
     * *Optional*. *True*, if the gift is displayed on the account's profile page; for gifts received on behalf of business accounts only
     */
    public val is_saved: Boolean? = null,
    /**
     * *Optional*. *True*, if the gift can be transferred to another owner; for gifts received on behalf of business accounts only
     */
    public val can_be_transferred: Boolean? = null,
    /**
     * *Optional*. Number of Telegram Stars that must be paid to transfer the gift; omitted if the bot cannot transfer the gift
     */
    public val transfer_star_count: Int? = null,
    /**
     * *Optional*. Point in time (Unix timestamp) when the gift can be transferred. If it is in the past, then the gift can be transferred now
     */
    public val next_transfer_date: Long? = null,
) : OwnedGift
