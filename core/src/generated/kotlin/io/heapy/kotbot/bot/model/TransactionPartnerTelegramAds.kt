package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Describes a withdrawal transaction to the Telegram Ads platform.
 */
@Serializable
public data class TransactionPartnerTelegramAds(
    /**
     * Type of the transaction partner, always "telegram_ads"
     */
    public val type: String,
) : TransactionPartner
