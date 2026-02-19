package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Describes a regular gift owned by a user or a chat.
 */
@Serializable
public data class OwnedGiftRegular(
    /**
     * Type of the gift, always "regular"
     */
    public val type: String,
    /**
     * Information about the regular gift
     */
    public val gift: Gift,
    /**
     * *Optional*. Unique identifier of the gift for the bot; for gifts received on behalf of business accounts only
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
     * *Optional*. Text of the message that was added to the gift
     */
    public val text: String? = null,
    /**
     * *Optional*. Special entities that appear in the text
     */
    public val entities: List<MessageEntity>? = null,
    /**
     * *Optional*. *True*, if the sender and gift text are shown only to the gift receiver; otherwise, everyone will be able to see them
     */
    public val is_private: Boolean? = null,
    /**
     * *Optional*. *True*, if the gift is displayed on the account's profile page; for gifts received on behalf of business accounts only
     */
    public val is_saved: Boolean? = null,
    /**
     * *Optional*. *True*, if the gift can be upgraded to a unique gift; for gifts received on behalf of business accounts only
     */
    public val can_be_upgraded: Boolean? = null,
    /**
     * *Optional*. *True*, if the gift was refunded and isn't available anymore
     */
    public val was_refunded: Boolean? = null,
    /**
     * *Optional*. Number of Telegram Stars that can be claimed by the receiver instead of the gift; omitted if the gift cannot be converted to Telegram Stars; for gifts received on behalf of business accounts only
     */
    public val convert_star_count: Int? = null,
    /**
     * *Optional*. Number of Telegram Stars that were paid for the ability to upgrade the gift
     */
    public val prepaid_upgrade_star_count: Int? = null,
    /**
     * *Optional*. *True*, if the gift's upgrade was purchased after the gift was sent; for gifts received on behalf of business accounts only
     */
    public val is_upgrade_separate: Boolean? = null,
    /**
     * *Optional*. Unique number reserved for this gift when upgraded. See the *number* field in [UniqueGift](https://core.telegram.org/bots/api/#uniquegift)
     */
    public val unique_gift_number: Int? = null,
) : OwnedGift
