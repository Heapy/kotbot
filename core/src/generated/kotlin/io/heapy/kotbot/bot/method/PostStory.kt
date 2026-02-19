package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.InputStoryContent
import io.heapy.kotbot.bot.model.MessageEntity
import io.heapy.kotbot.bot.model.Story
import io.heapy.kotbot.bot.model.StoryArea
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Posts a story on behalf of a managed business account. Requires the *can_manage_stories* business bot right. Returns [Story](https://core.telegram.org/bots/api/#story) on success.
 */
@Serializable
public data class PostStory(
    /**
     * Unique identifier of the business connection
     */
    public val business_connection_id: String,
    /**
     * Content of the story
     */
    public val content: InputStoryContent,
    /**
     * Period after which the story is moved to the archive, in seconds; must be one of `6 * 3600`, `12 * 3600`, `86400`, or `2 * 86400`
     */
    public val active_period: Int,
    /**
     * Caption of the story, 0-2048 characters after entities parsing
     */
    public val caption: String? = null,
    /**
     * Mode for parsing entities in the story caption. See [formatting options](https://core.telegram.org/bots/api/#formatting-options) for more details.
     */
    public val parse_mode: String? = null,
    /**
     * A JSON-serialized list of special entities that appear in the caption, which can be specified instead of *parse_mode*
     */
    public val caption_entities: List<MessageEntity>? = null,
    /**
     * A JSON-serialized list of clickable areas to be shown on the story
     */
    public val areas: List<StoryArea>? = null,
    /**
     * Pass *True* to keep the story accessible after it expires
     */
    public val post_to_chat_page: Boolean? = null,
    /**
     * Pass *True* if the content of the story must be protected from forwarding and screenshotting
     */
    public val protect_content: Boolean? = null,
) : Method<PostStory, Story> by Companion {
    public companion object : Method<PostStory, Story> {
        override val _deserializer: KSerializer<Response<Story>> =
                Response.serializer(Story.serializer())

        override val _serializer: KSerializer<PostStory> = serializer()

        override val _name: String = "postStory"
    }
}
