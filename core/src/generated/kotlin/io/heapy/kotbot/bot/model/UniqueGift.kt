package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object describes a unique gift that was upgraded from a regular gift.
 */
@Serializable
public data class UniqueGift(
    /**
     * Identifier of the regular gift from which the gift was upgraded
     */
    public val gift_id: String,
    /**
     * Human-readable name of the regular gift from which this unique gift was upgraded
     */
    public val base_name: String,
    /**
     * Unique name of the gift. This name can be used in `https://t.me/nft/...` links and story areas
     */
    public val name: String,
    /**
     * Unique number of the upgraded gift among gifts upgraded from the same regular gift
     */
    public val number: Int,
    /**
     * Model of the gift
     */
    public val model: UniqueGiftModel,
    /**
     * Symbol of the gift
     */
    public val symbol: UniqueGiftSymbol,
    /**
     * Backdrop of the gift
     */
    public val backdrop: UniqueGiftBackdrop,
    /**
     * *Optional*. *True*, if the original regular gift was exclusively purchaseable by Telegram Premium subscribers
     */
    public val is_premium: Boolean? = null,
    /**
     * *Optional*. *True*, if the gift was used to craft another gift and isn't available anymore
     */
    public val is_burned: Boolean? = null,
    /**
     * *Optional*. *True*, if the gift is assigned from the TON blockchain and can't be resold or transferred in Telegram
     */
    public val is_from_blockchain: Boolean? = null,
    /**
     * *Optional*. The color scheme that can be used by the gift's owner for the chat's name, replies to messages and link previews; for business account gifts and gifts that are currently on sale only
     */
    public val colors: UniqueGiftColors? = null,
    /**
     * *Optional*. Information about the chat that published the gift
     */
    public val publisher_chat: Chat? = null,
)
