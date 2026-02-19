package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.Story
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Reposts a story on behalf of a business account from another business account. Both business accounts must be managed by the same bot, and the story on the source account must have been posted (or reposted) by the bot. Requires the *can_manage_stories* business bot right for both business accounts. Returns [Story](https://core.telegram.org/bots/api/#story) on success.
 */
@Serializable
public data class RepostStory(
    /**
     * Unique identifier of the business connection
     */
    public val business_connection_id: String,
    /**
     * Unique identifier of the chat which posted the story that should be reposted
     */
    public val from_chat_id: Long,
    /**
     * Unique identifier of the story that should be reposted
     */
    public val from_story_id: Int,
    /**
     * Period after which the story is moved to the archive, in seconds; must be one of `6 * 3600`, `12 * 3600`, `86400`, or `2 * 86400`
     */
    public val active_period: Int,
    /**
     * Pass *True* to keep the story accessible after it expires
     */
    public val post_to_chat_page: Boolean? = null,
    /**
     * Pass *True* if the content of the story must be protected from forwarding and screenshotting
     */
    public val protect_content: Boolean? = null,
) : Method<RepostStory, Story> by Companion {
    public companion object : Method<RepostStory, Story> {
        override val _deserializer: KSerializer<Response<Story>> =
                Response.serializer(Story.serializer())

        override val _serializer: KSerializer<RepostStory> = serializer()

        override val _name: String = "repostStory"
    }
}
