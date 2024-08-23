package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Int
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * This object contains information about a message that is being replied to, which may come from another chat or forum topic.
 */
@Serializable
public data class ExternalReplyInfo(
    /**
     * Origin of the message replied to by the given message
     */
    public val origin: MessageOrigin,
    /**
     * *Optional*. Chat the original message belongs to. Available only if the chat is a supergroup or a channel.
     */
    public val chat: Chat? = null,
    /**
     * *Optional*. Unique message identifier inside the original chat. Available only if the original chat is a supergroup or a channel.
     */
    public val message_id: Int? = null,
    /**
     * *Optional*. Options used for link preview generation for the original message, if it is a text message
     */
    public val link_preview_options: LinkPreviewOptions? = null,
    /**
     * *Optional*. Message is an animation, information about the animation
     */
    public val animation: Animation? = null,
    /**
     * *Optional*. Message is an audio file, information about the file
     */
    public val audio: Audio? = null,
    /**
     * *Optional*. Message is a general file, information about the file
     */
    public val document: Document? = null,
    /**
     * *Optional*. Message is a photo, available sizes of the photo
     */
    public val photo: List<PhotoSize>? = null,
    /**
     * *Optional*. Message is a sticker, information about the sticker
     */
    public val sticker: Sticker? = null,
    /**
     * *Optional*. Message is a forwarded story
     */
    public val story: Story? = null,
    /**
     * *Optional*. Message is a video, information about the video
     */
    public val video: Video? = null,
    /**
     * *Optional*. Message is a [video note](https://telegram.org/blog/video-messages-and-telescope), information about the video message
     */
    public val video_note: VideoNote? = null,
    /**
     * *Optional*. Message is a voice message, information about the file
     */
    public val voice: Voice? = null,
    /**
     * *Optional*. *True*, if the message media is covered by a spoiler animation
     */
    public val has_media_spoiler: Boolean? = null,
    /**
     * *Optional*. Message is a shared contact, information about the contact
     */
    public val contact: Contact? = null,
    /**
     * *Optional*. Message is a dice with random value
     */
    public val dice: Dice? = null,
    /**
     * *Optional*. Message is a game, information about the game. [More about games &raquo;](https://core.telegram.org/bots/api/#games)
     */
    public val game: Game? = null,
    /**
     * *Optional*. Message is a scheduled giveaway, information about the giveaway
     */
    public val giveaway: Giveaway? = null,
    /**
     * *Optional*. A giveaway with public winners was completed
     */
    public val giveaway_winners: GiveawayWinners? = null,
    /**
     * *Optional*. Message is an invoice for a [payment](https://core.telegram.org/bots/api/#payments), information about the invoice. [More about payments &raquo;](https://core.telegram.org/bots/api/#payments)
     */
    public val invoice: Invoice? = null,
    /**
     * *Optional*. Message is a shared location, information about the location
     */
    public val location: Location? = null,
    /**
     * *Optional*. Message is a native poll, information about the poll
     */
    public val poll: Poll? = null,
    /**
     * *Optional*. Message is a venue, information about the venue
     */
    public val venue: Venue? = null,
)
