package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Contains information about the start page settings of a Telegram Business account.
 */
@Serializable
public data class BusinessIntro(
    /**
     * *Optional*. Title text of the business intro
     */
    public val title: String? = null,
    /**
     * *Optional*. Message text of the business intro
     */
    public val message: String? = null,
    /**
     * *Optional*. Sticker of the business intro
     */
    public val sticker: Sticker? = null,
)
