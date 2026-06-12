package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to process a received chat join request query. Returns *True* on success.
 */
@Serializable
public data class AnswerChatJoinRequestQuery(
    /**
     * Unique identifier of the join request query
     */
    public val chat_join_request_query_id: String,
    /**
     * Result of the query. Must be either "approve" to allow the user to join the chat, "decline" to disallow the user to join the chat, or "queue" to leave the decision to other administrators.
     */
    public val result: String,
) : Method<AnswerChatJoinRequestQuery, Boolean> by Companion {
    public companion object : Method<AnswerChatJoinRequestQuery, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<AnswerChatJoinRequestQuery> = serializer()

        override val _name: String = "answerChatJoinRequestQuery"
    }
}
