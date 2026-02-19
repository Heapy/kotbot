package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Describes a service message about a regular gift that was sent or received.
 */
@Serializable
public data class GiftInfo(
    /**
     * Information about the gift
     */
    public val gift: Gift,
    /**
     * *Optional*. Unique identifier of the received gift for the bot; only present for gifts received on behalf of business accounts
     */
    public val owned_gift_id: String? = null,
    /**
     * *Optional*. Number of Telegram Stars that can be claimed by the receiver by converting the gift; omitted if conversion to Telegram Stars is impossible
     */
    public val convert_star_count: Int? = null,
    /**
     * *Optional*. Number of Telegram Stars that were prepaid for the ability to upgrade the gift
     */
    public val prepaid_upgrade_star_count: Int? = null,
    /**
     * *Optional*. *True*, if the gift's upgrade was purchased after the gift was sent
     */
    public val is_upgrade_separate: Boolean? = null,
    /**
     * *Optional*. *True*, if the gift can be upgraded to a unique gift
     */
    public val can_be_upgraded: Boolean? = null,
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
     * *Optional*. Unique number reserved for this gift when upgraded. See the *number* field in [UniqueGift](https://core.telegram.org/bots/api/#uniquegift)
     */
    public val unique_gift_number: Int? = null,
)
