package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.InlineQueryResult
import io.heapy.kotbot.bot.model.InlineQueryResultsButton
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to send answers to an inline query. On success, *True* is returned.  
 * No more than **50** results per query are allowed.
 */
@Serializable
public data class AnswerInlineQuery(
    /**
     * Unique identifier for the answered query
     */
    public val inline_query_id: String,
    /**
     * A JSON-serialized array of results for the inline query
     */
    public val results: List<InlineQueryResult>,
    /**
     * The maximum amount of time in seconds that the result of the inline query may be cached on the server. Defaults to 300.
     */
    public val cache_time: Int? = null,
    /**
     * Pass *True* if results may be cached on the server side only for the user that sent the query. By default, results may be returned to any user who sends the same query.
     */
    public val is_personal: Boolean? = null,
    /**
     * Pass the offset that a client should send in the next query with the same text to receive more results. Pass an empty string if there are no more results or if you don't support pagination. Offset length can't exceed 64 bytes.
     */
    public val next_offset: String? = null,
    /**
     * A JSON-serialized object describing a button to be shown above inline query results
     */
    public val button: InlineQueryResultsButton? = null,
) : Method<AnswerInlineQuery, Boolean> by Companion {
    public companion object : Method<AnswerInlineQuery, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<AnswerInlineQuery> = serializer()

        override val _name: String = "answerInlineQuery"
    }
}
