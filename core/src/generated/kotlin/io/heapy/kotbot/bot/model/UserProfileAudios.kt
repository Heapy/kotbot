package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * This object represents the audios displayed on a user's profile.
 */
@Serializable
public data class UserProfileAudios(
    /**
     * Total number of profile audios for the target user
     */
    public val total_count: Int,
    /**
     * Requested profile audios
     */
    public val audios: List<Audio>,
)
