package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.ForumTopic
import kotlin.Int
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to create a topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have the *can_manage_topics* administrator rights. Returns information about the created topic as a [ForumTopic](https://core.telegram.org/bots/api/#forumtopic) object.
 */
@Serializable
public data class CreateForumTopic(
    /**
     * Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
     */
    public val chat_id: ChatId,
    /**
     * Topic name, 1-128 characters
     */
    public val name: String,
    /**
     * Color of the topic icon in RGB format. Currently, must be one of 7322096 (0x6FB9F0), 16766590 (0xFFD67E), 13338331 (0xCB86DB), 9367192 (0x8EEE98), 16749490 (0xFF93B2), or 16478047 (0xFB6F5F)
     */
    public val icon_color: Int? = null,
    /**
     * Unique identifier of the custom emoji shown as the topic icon. Use [getForumTopicIconStickers](https://core.telegram.org/bots/api/#getforumtopiciconstickers) to get all allowed custom emoji identifiers.
     */
    public val icon_custom_emoji_id: String? = null,
) : Method<CreateForumTopic, ForumTopic> by Companion {
    public companion object : Method<CreateForumTopic, ForumTopic> {
        override val _deserializer: KSerializer<Response<ForumTopic>> =
                Response.serializer(ForumTopic.serializer())

        override val _serializer: KSerializer<CreateForumTopic> = serializer()

        override val _name: String = "createForumTopic"
    }
}
