package io.heapy.kotbot.bot.join

import io.heapy.kotbot.infra.configuration.JoinChallengeConfiguration
import io.heapy.kotbot.infra.configuration.KnownChatsConfiguration
import kotlin.time.Duration

class ResolvedJoinChallengeConfig(
    private val config: JoinChallengeConfiguration,
    private val groupsConfig: KnownChatsConfiguration,
) {
    private val chatIdToName: Map<Long, String> by lazy {
        groupsConfig.allowedGroups.entries.associate { (name, id) -> id to name }
    }

    /** Maps each chat ID to the list of all chat IDs in its group (including itself). */
    private val chatIdToGroupChatIds: Map<Long, List<Long>> by lazy {
        val nameToId = groupsConfig.allowedGroups
        val result = mutableMapOf<Long, List<Long>>()
        for ((_, chatNames) in config.chatGroups) {
            val groupIds = chatNames.mapNotNull { nameToId[it] }
            for (id in groupIds) {
                result[id] = groupIds
            }
        }
        result
    }

    /** Returns all chat IDs in the same group as [chatId], or a singleton list if not in any group. */
    fun chatIdsInGroup(chatId: Long): List<Long> =
        chatIdToGroupChatIds[chatId] ?: listOf(chatId)

    fun maxAttemptsForChat(chatId: Long): Int {
        val chatName = chatIdToName[chatId] ?: return config.defaultMaxAttempts
        return config.chatMaxAttempts[chatName] ?: config.defaultMaxAttempts
    }

    val sessionTimeout: Duration get() = config.sessionTimeout
    val retryCooldown: Duration get() = config.retryCooldown
}
