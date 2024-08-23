package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * This object represents a message.
 */
@Serializable
public data class Message(
    /**
     * Unique message identifier inside this chat
     */
    public val message_id: Int,
    /**
     * *Optional*. Unique identifier of a message thread to which the message belongs; for supergroups only
     */
    public val message_thread_id: Int? = null,
    /**
     * *Optional*. Sender of the message; may be empty for messages sent to channels. For backward compatibility, if the message was sent on behalf of a chat, the field contains a fake sender user in non-channel chats
     */
    public val from: User? = null,
    /**
     * *Optional*. Sender of the message when sent on behalf of a chat. For example, the supergroup itself for messages sent by its anonymous administrators or a linked channel for messages automatically forwarded to the channel's discussion group. For backward compatibility, if the message was sent on behalf of a chat, the field *from* contains a fake sender user in non-channel chats.
     */
    public val sender_chat: Chat? = null,
    /**
     * *Optional*. If the sender of the message boosted the chat, the number of boosts added by the user
     */
    public val sender_boost_count: Int? = null,
    /**
     * *Optional*. The bot that actually sent the message on behalf of the business account. Available only for outgoing messages sent on behalf of the connected business account.
     */
    public val sender_business_bot: User? = null,
    /**
     * Date the message was sent in Unix time. It is always a positive number, representing a valid date.
     */
    public val date: Long,
    /**
     * *Optional*. Unique identifier of the business connection from which the message was received. If non-empty, the message belongs to a chat of the corresponding business account that is independent from any potential bot chat which might share the same identifier.
     */
    public val business_connection_id: String? = null,
    /**
     * Chat the message belongs to
     */
    public val chat: Chat,
    /**
     * *Optional*. Information about the original message for forwarded messages
     */
    public val forward_origin: MessageOrigin? = null,
    /**
     * *Optional*. *True*, if the message is sent to a forum topic
     */
    public val is_topic_message: Boolean? = null,
    /**
     * *Optional*. *True*, if the message is a channel post that was automatically forwarded to the connected discussion group
     */
    public val is_automatic_forward: Boolean? = null,
    /**
     * *Optional*. For replies in the same chat and message thread, the original message. Note that the Message object in this field will not contain further *reply_to_message* fields even if it itself is a reply.
     */
    public val reply_to_message: Message? = null,
    /**
     * *Optional*. Information about the message that is being replied to, which may come from another chat or forum topic
     */
    public val external_reply: ExternalReplyInfo? = null,
    /**
     * *Optional*. For replies that quote part of the original message, the quoted part of the message
     */
    public val quote: TextQuote? = null,
    /**
     * *Optional*. For replies to a story, the original story
     */
    public val reply_to_story: Story? = null,
    /**
     * *Optional*. Bot through which the message was sent
     */
    public val via_bot: User? = null,
    /**
     * *Optional*. Date the message was last edited in Unix time
     */
    public val edit_date: Long? = null,
    /**
     * *Optional*. *True*, if the message can't be forwarded
     */
    public val has_protected_content: Boolean? = null,
    /**
     * *Optional*. True, if the message was sent by an implicit action, for example, as an away or a greeting business message, or as a scheduled message
     */
    public val is_from_offline: Boolean? = null,
    /**
     * *Optional*. The unique identifier of a media message group this message belongs to
     */
    public val media_group_id: String? = null,
    /**
     * *Optional*. Signature of the post author for messages in channels, or the custom title of an anonymous group administrator
     */
    public val author_signature: String? = null,
    /**
     * *Optional*. For text messages, the actual UTF-8 text of the message
     */
    public val text: String? = null,
    /**
     * *Optional*. For text messages, special entities like usernames, URLs, bot commands, etc. that appear in the text
     */
    public val entities: List<MessageEntity>? = null,
    /**
     * *Optional*. Options used for link preview generation for the message, if it is a text message and link preview options were changed
     */
    public val link_preview_options: LinkPreviewOptions? = null,
    /**
     * *Optional*. Unique identifier of the message effect added to the message
     */
    public val effect_id: String? = null,
    /**
     * *Optional*. Message is an animation, information about the animation. For backward compatibility, when this field is set, the *document* field will also be set
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
     * *Optional*. Message contains paid media; information about the paid media
     */
    public val paid_media: PaidMediaInfo? = null,
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
     * *Optional*. Caption for the animation, audio, document, paid media, photo, video or voice
     */
    public val caption: String? = null,
    /**
     * *Optional*. For messages with a caption, special entities like usernames, URLs, bot commands, etc. that appear in the caption
     */
    public val caption_entities: List<MessageEntity>? = null,
    /**
     * *Optional*. True, if the caption must be shown above the message media
     */
    public val show_caption_above_media: Boolean? = null,
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
     * *Optional*. Message is a native poll, information about the poll
     */
    public val poll: Poll? = null,
    /**
     * *Optional*. Message is a venue, information about the venue. For backward compatibility, when this field is set, the *location* field will also be set
     */
    public val venue: Venue? = null,
    /**
     * *Optional*. Message is a shared location, information about the location
     */
    public val location: Location? = null,
    /**
     * *Optional*. New members that were added to the group or supergroup and information about them (the bot itself may be one of these members)
     */
    public val new_chat_members: List<User>? = null,
    /**
     * *Optional*. A member was removed from the group, information about them (this member may be the bot itself)
     */
    public val left_chat_member: User? = null,
    /**
     * *Optional*. A chat title was changed to this value
     */
    public val new_chat_title: String? = null,
    /**
     * *Optional*. A chat photo was change to this value
     */
    public val new_chat_photo: List<PhotoSize>? = null,
    /**
     * *Optional*. Service message: the chat photo was deleted
     */
    public val delete_chat_photo: Boolean? = null,
    /**
     * *Optional*. Service message: the group has been created
     */
    public val group_chat_created: Boolean? = null,
    /**
     * *Optional*. Service message: the supergroup has been created. This field can't be received in a message coming through updates, because bot can't be a member of a supergroup when it is created. It can only be found in reply_to_message if someone replies to a very first message in a directly created supergroup.
     */
    public val supergroup_chat_created: Boolean? = null,
    /**
     * *Optional*. Service message: the channel has been created. This field can't be received in a message coming through updates, because bot can't be a member of a channel when it is created. It can only be found in reply_to_message if someone replies to a very first message in a channel.
     */
    public val channel_chat_created: Boolean? = null,
    /**
     * *Optional*. Service message: auto-delete timer settings changed in the chat
     */
    public val message_auto_delete_timer_changed: MessageAutoDeleteTimerChanged? = null,
    /**
     * *Optional*. The group has been migrated to a supergroup with the specified identifier. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this identifier.
     */
    public val migrate_to_chat_id: Long? = null,
    /**
     * *Optional*. The supergroup has been migrated from a group with the specified identifier. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this identifier.
     */
    public val migrate_from_chat_id: Long? = null,
    /**
     * *Optional*. Specified message was pinned. Note that the Message object in this field will not contain further *reply_to_message* fields even if it itself is a reply.
     */
    public val pinned_message: MaybeInaccessibleMessage? = null,
    /**
     * *Optional*. Message is an invoice for a [payment](https://core.telegram.org/bots/api/#payments), information about the invoice. [More about payments &raquo;](https://core.telegram.org/bots/api/#payments)
     */
    public val invoice: Invoice? = null,
    /**
     * *Optional*. Message is a service message about a successful payment, information about the payment. [More about payments &raquo;](https://core.telegram.org/bots/api/#payments)
     */
    public val successful_payment: SuccessfulPayment? = null,
    /**
     * *Optional*. Message is a service message about a refunded payment, information about the payment. [More about payments &raquo;](https://core.telegram.org/bots/api/#payments)
     */
    public val refunded_payment: RefundedPayment? = null,
    /**
     * *Optional*. Service message: users were shared with the bot
     */
    public val users_shared: UsersShared? = null,
    /**
     * *Optional*. Service message: a chat was shared with the bot
     */
    public val chat_shared: ChatShared? = null,
    /**
     * *Optional*. The domain name of the website on which the user has logged in. [More about Telegram Login &raquo;](https://core.telegram.org/widgets/login)
     */
    public val connected_website: String? = null,
    /**
     * *Optional*. Service message: the user allowed the bot to write messages after adding it to the attachment or side menu, launching a Web App from a link, or accepting an explicit request from a Web App sent by the method [requestWriteAccess](https://core.telegram.org/bots/webapps#initializing-mini-apps)
     */
    public val write_access_allowed: WriteAccessAllowed? = null,
    /**
     * *Optional*. Telegram Passport data
     */
    public val passport_data: PassportData? = null,
    /**
     * *Optional*. Service message. A user in the chat triggered another user's proximity alert while sharing Live Location.
     */
    public val proximity_alert_triggered: ProximityAlertTriggered? = null,
    /**
     * *Optional*. Service message: user boosted the chat
     */
    public val boost_added: ChatBoostAdded? = null,
    /**
     * *Optional*. Service message: chat background set
     */
    public val chat_background_set: ChatBackground? = null,
    /**
     * *Optional*. Service message: forum topic created
     */
    public val forum_topic_created: ForumTopicCreated? = null,
    /**
     * *Optional*. Service message: forum topic edited
     */
    public val forum_topic_edited: ForumTopicEdited? = null,
    /**
     * *Optional*. Service message: forum topic closed
     */
    public val forum_topic_closed: ForumTopicClosed? = null,
    /**
     * *Optional*. Service message: forum topic reopened
     */
    public val forum_topic_reopened: ForumTopicReopened? = null,
    /**
     * *Optional*. Service message: the 'General' forum topic hidden
     */
    public val general_forum_topic_hidden: GeneralForumTopicHidden? = null,
    /**
     * *Optional*. Service message: the 'General' forum topic unhidden
     */
    public val general_forum_topic_unhidden: GeneralForumTopicUnhidden? = null,
    /**
     * *Optional*. Service message: a scheduled giveaway was created
     */
    public val giveaway_created: GiveawayCreated? = null,
    /**
     * *Optional*. The message is a scheduled giveaway message
     */
    public val giveaway: Giveaway? = null,
    /**
     * *Optional*. A giveaway with public winners was completed
     */
    public val giveaway_winners: GiveawayWinners? = null,
    /**
     * *Optional*. Service message: a giveaway without public winners was completed
     */
    public val giveaway_completed: GiveawayCompleted? = null,
    /**
     * *Optional*. Service message: video chat scheduled
     */
    public val video_chat_scheduled: VideoChatScheduled? = null,
    /**
     * *Optional*. Service message: video chat started
     */
    public val video_chat_started: VideoChatStarted? = null,
    /**
     * *Optional*. Service message: video chat ended
     */
    public val video_chat_ended: VideoChatEnded? = null,
    /**
     * *Optional*. Service message: new participants invited to a video chat
     */
    public val video_chat_participants_invited: VideoChatParticipantsInvited? = null,
    /**
     * *Optional*. Service message: data sent by a Web App
     */
    public val web_app_data: WebAppData? = null,
    /**
     * *Optional*. Inline keyboard attached to the message. `login_url` buttons are represented as ordinary `url` buttons.
     */
    public val reply_markup: InlineKeyboardMarkup? = null,
) : MaybeInaccessibleMessage
