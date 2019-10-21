package io.heapy.kotbot.bot.rule

/**
 * Return only single action.
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Action.only(): List<Action> = listOf(this)

/**
 * Return no actions from rule.
 */
@Suppress("NOTHING_TO_INLINE")
inline fun noActions(): List<Action> = emptyList()
