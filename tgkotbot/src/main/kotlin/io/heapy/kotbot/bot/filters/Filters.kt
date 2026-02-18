package io.heapy.kotbot.bot.filters

import io.heapy.kotbot.bot.TypedUpdate

fun interface Filter {
    /**
     * Returns true if this [TypedUpdate] can be processed by bot, false otherwise.
     */
    suspend fun predicate(
        update: TypedUpdate,
    ): Boolean

    companion object
}

/**
 * Create a single filter from multiple filters.
 * Returns false if any of the filters returns false.
 */
fun Filter.Companion.combine(filters: List<Filter>): Filter {
    return Filter { update ->
        filters.all { filter -> filter.predicate(update) }
    }
}
