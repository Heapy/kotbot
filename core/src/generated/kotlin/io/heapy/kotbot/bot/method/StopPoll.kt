package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.InlineKeyboardMarkup
import io.heapy.kotbot.bot.model.Poll
import kotlin.Int
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to stop a poll which was sent by the bot. On success, the stopped [Poll](https://core.telegram.org/bots/api/#poll) is returned.
 */
@Serializable
public data class StopPoll(
    /**
     * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     */
    public val chat_id: ChatId,
    /**
     * Identifier of the original message with the poll
     */
    public val message_id: Int,
    /**
     * A JSON-serialized object for a new message [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards).
     */
    public val reply_markup: InlineKeyboardMarkup? = null,
) : Method<StopPoll, Poll> by Companion {
    public companion object : Method<StopPoll, Poll> {
        override val _deserializer: KSerializer<Response<Poll>> =
                Response.serializer(Poll.serializer())

        override val _serializer: KSerializer<StopPoll> = serializer()

        override val _name: String = "stopPoll"
    }
}
