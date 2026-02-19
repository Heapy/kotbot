package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Upgrades a given regular gift to a unique gift. Requires the *can_transfer_and_upgrade_gifts* business bot right. Additionally requires the *can_transfer_stars* business bot right if the upgrade is paid. Returns *True* on success.
 */
@Serializable
public data class UpgradeGift(
    /**
     * Unique identifier of the business connection
     */
    public val business_connection_id: String,
    /**
     * Unique identifier of the regular gift that should be upgraded to a unique one
     */
    public val owned_gift_id: String,
    /**
     * Pass *True* to keep the original gift text, sender and receiver in the upgraded gift
     */
    public val keep_original_details: Boolean? = null,
    /**
     * The amount of Telegram Stars that will be paid for the upgrade from the business account balance. If `gift.prepaid_upgrade_star_count > 0`, then pass 0, otherwise, the *can_transfer_stars* business bot right is required and `gift.upgrade_star_count` must be passed.
     */
    public val star_count: Int? = null,
) : Method<UpgradeGift, Boolean> by Companion {
    public companion object : Method<UpgradeGift, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<UpgradeGift> = serializer()

        override val _name: String = "upgradeGift"
    }
}
