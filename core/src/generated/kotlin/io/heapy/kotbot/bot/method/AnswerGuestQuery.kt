package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.InlineQueryResult
import io.heapy.kotbot.bot.model.SentGuestMessage
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to reply to a received guest message. On success, a [SentGuestMessage](https://core.telegram.org/bots/api/#sentguestmessage) object is returned.
 */
@Serializable
public data class AnswerGuestQuery(
    /**
     * Unique identifier for the query to be answered
     */
    public val guest_query_id: String,
    /**
     * A JSON-serialized object describing the message to be sent
     */
    public val result: InlineQueryResult,
) : Method<AnswerGuestQuery, SentGuestMessage> by Companion {
    public companion object : Method<AnswerGuestQuery, SentGuestMessage> {
        override val _deserializer: KSerializer<Response<SentGuestMessage>> =
                Response.serializer(SentGuestMessage.serializer())

        override val _serializer: KSerializer<AnswerGuestQuery> = serializer()

        override val _name: String = "answerGuestQuery"
    }
}
