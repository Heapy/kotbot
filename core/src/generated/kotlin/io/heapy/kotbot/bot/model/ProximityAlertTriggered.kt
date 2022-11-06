package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlinx.serialization.Serializable

/**
 * This object represents the content of a service message, sent whenever a user in the chat triggers a proximity alert set by another user.
 */
@Serializable
public data class ProximityAlertTriggered(
    /**
     * User that triggered the alert
     */
    public val traveler: User,
    /**
     * User that set the alert
     */
    public val watcher: User,
    /**
     * The distance between the users
     */
    public val distance: Int,
)
