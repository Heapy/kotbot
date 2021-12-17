package io.heapy.kotbot.bot

import kotlinx.serialization.Serializable

@Serializable
public data class Update(
    /**
     * The update's unique identifier. Update identifiers start from a certain
     * positive number and increase sequentially. This ID becomes especially
     * handy if you're using Webhooks, since it allows you to ignore repeated
     * updates or to restore the correct update sequence, should they get out
     * of order. If there are no new updates for at least a week, then
     * identifier of the next update will be chosen randomly instead of sequentially.
     */
    val update_id: Int,
    /**
     * Optional. New incoming message of any kind — text, photo, sticker, etc.
     */
    val message: Message? = null,
    /**
     * Optional. New version of a message that is known to the bot and was edited
     */
    val edited_message: Message? = null,
    /**
     * Optional. New incoming channel post of any kind — text, photo, sticker, etc.
     */
    val channel_post: Message? = null,
    /**
     * Optional. New version of a channel post that is known to the bot and was edited
     */
    val edited_channel_post: Message? = null,
    /**
     * Optional. New incoming inline query
     */
    val inline_query: Message? = null,
    /**
     * Optional. The result of an inline query that was chosen by a user and
     * sent to their chat partner. Please see our documentation on the feedback
     * collecting for details on how to enable these updates for your bot.
     */
    val chosen_inline_result: ChosenInlineResult? = null,
    /**
     * Optional. New incoming callback query
     */
    val callback_query: CallbackQuery? = null,
    /**
     * Optional. New incoming shipping query. Only for invoices with flexible price
     */
    val shipping_query: ShippingQuery? = null,
    /**
     * Optional. New incoming pre-checkout query. Contains full information about checkout
     */
    val pre_checkout_query: PreCheckoutQuery? = null,
    /**
     * Optional. New poll state. Bots receive only updates about stopped polls
     * and polls, which are sent by the bot
     */
    val poll: Poll? = null,
    /**
     * Optional. A user changed their answer in a non-anonymous poll. Bots
     * receive new votes only in polls that were sent by the bot itself.
     */
    val poll_answer: PollAnswer? = null,
    /**
     * Optional. The bot's chat member status was updated in a chat. For private
     * chats, this update is received only when the bot is blocked or unblocked by the user.
     */
    val my_chat_member: ChatMemberUpdated? = null,
    /**
     * Optional. A chat member's status was updated in a chat. The bot must be
     * an administrator in the chat and must explicitly specify “chat_member”
     * in the list of allowed_updates to receive these updates.
     */
    val chat_member: ChatMemberUpdated? = null,
) {
    @Serializable
    public data class Message(
        /**
         * Unique message identifier inside this chat
         */
        val message_id: Int,
        /**
         * Optional. Sender, empty for messages sent to channels
         */
        val from: User? = null,
        /**
         * Optional. Sender of the message, sent on behalf of a chat. The
         * channel itself for channel messages. The supergroup itself for
         * messages from anonymous group administrators. The linked channel
         * for messages automatically forwarded to the discussion group
         */
        val sender_chat: Chat? = null,
        /**
         * Date the message was sent in Unix time
         */
        val date: Int,
        /**
         * Conversation the message belongs to
         */
        val chat: Chat,
        /**
         * Optional. For forwarded messages, sender of the original message
         */
        val forward_from: User? = null,
        /**
         * Optional. For messages forwarded from channels or from anonymous
         * administrators, information about the original sender chat
         */
        val forward_from_chat: Chat? = null,
        /**
         * Optional. For messages forwarded from channels, identifier of the
         * original message in the channel
         */
        val forward_from_message_id: Int? = null,
        /**
         * Optional. For messages forwarded from channels, signature of
         * the post author if present
         */
        val forward_signature: String? = null,
        /**
         * Optional. Sender's name for messages forwarded from users who disallow
         * adding a link to their account in forwarded messages
         */
        val forward_sender_name: String? = null,
        /**
         * Optional. For forwarded messages, date the original message was
         * sent in Unix time
         */
        val forward_date: Int? = null,
        /**
         * Optional. For replies, the original message. Note that the Message
         * object in this field will not contain further reply_to_message
         * fields even if it itself is a reply.
         */
        val reply_to_message: Message? = null,
        /**
         * Optional. Bot through which the message was sent
         */
        val via_bot: User? = null,
        /**
         * Optional. Date the message was last edited in Unix time
         */
        val edit_date: Int? = null,
        /**
         * Optional. The unique identifier of a media message group
         * this message belongs to
         */
        val media_group_id: String? = null,
        /**
         * Optional. Signature of the post author for messages in channels, or
         * the custom title of an anonymous group administrator
         */
        val author_signature: String? = null,
        /**
         * Optional. For text messages, the actual UTF-8 text of the message,
         * 0-4096 characters
         */
        val text: String? = null,
        /**
         * Optional. For text messages, special entities like usernames, URLs,
         * bot commands, etc. that appear in the text
         */
        val entities: List<MessageEntity>? = null,
        /**
         * Optional. Message is an animation, information about the animation.
         * For backward compatibility, when this field is set,
         * the document field will also be set
         */
        val animation: Animation? = null,
        /**
         * Optional. Message is an audio file, information about the file
         */
        val audio: Audio? = null,
        /**
         * Optional. Message is a general file, information about the file
         */
        val document: Document? = null,
        /**
         * Optional. Message is a photo, available sizes of the photo
         */
        val photo: List<PhotoSize>? = null,
        /**
         * Optional. Message is a sticker, information about the sticker
         */
        val sticker: Sticker? = null,
        /**
         * Optional. Message is a video, information about the video
         */
        val video: Video? = null,
        /**
         * Optional. Message is a video note, information about the video message
         */
        val video_note: VideoNote? = null,
        /**
         * Optional. Message is a voice message, information about the file
         */
        val voice: Voice? = null,
        /**
         * Optional. Caption for the animation, audio, document, photo,
         * video or voice, 0-1024 characters
         */
        val caption: String? = null,
        /**
         * Optional. For messages with a caption, special entities like usernames,
         * URLs, bot commands, etc. that appear in the caption
         */
        val caption_entities: List<MessageEntity>? = null,
        /**
         * Optional. Message is a shared contact, information about the contact
         */
        val contact: Contact? = null,
        /**
         * Optional. Message is a dice with random value
         */
        val dice: Dice? = null,
        /**
         * Optional. Message is a game, information about the game
         */
        val game: Game? = null,
        /**
         * Optional. Message is a native poll, information about the poll
         */
        val poll: Poll? = null,
        /**
         * Optional. Message is a venue, information about the venue. For
         * backward compatibility, when this field is set, the location
         * field will also be set
         */
        val venue: Venue? = null,
        /**
         * Optional. Message is a shared location, information about the location
         */
        val location: Location? = null,
        /**
         * Optional. New members that were added to the group or supergroup
         * and information about them (the bot itself may be one of these members)
         */
        val new_chat_members: List<User>? = null,
        /**
         * Optional. A member was removed from the group, information about
         * them (this member may be the bot itself)
         */
        val left_chat_member: List<User>? = null,
        /**
         * Optional. A chat title was changed to this value
         */
        val new_chat_title: String? = null,
        /**
         * Optional. A chat photo was change to this value
         */
        val new_chat_photo: List<PhotoSize>? = null,
        /**
         * Optional. Service message: the chat photo was deleted
         */
        val delete_chat_photo: Boolean? = null,
        /**
         * Optional. Service message: the group has been created
         */
        val group_chat_created: Boolean? = null,
        /**
         * Optional. Service message: the supergroup has been created. This
         * field can't be received in a message coming through updates, because
         * bot can't be a member of a supergroup when it is created. It can only
         * be found in reply_to_message if someone replies to a very first
         * message in a directly created supergroup.
         */
        val supergroup_chat_created: Boolean? = null,
        /**
         * Optional. Service message: the channel has been created.
         * This field can't be received in a message coming through updates,
         * because bot can't be a member of a channel when it is created.
         * It can only be found in reply_to_message if someone replies
         * to a very first message in a channel.
         */
        val channel_chat_created: Boolean? = null,
        /**
         * Optional. Service message: auto-delete timer settings changed in the chat
         */
        val message_auto_delete_timer_changed: MessageAutoDeleteTimerChanged? = null,
        /**
         * Optional. The group has been migrated to a supergroup with the
         * specified identifier. This number may have more than 32 significant
         * bits and some programming languages may have difficulty/silent
         * defects in interpreting it. But it has at most 52 significant bits,
         * so a signed 64-bit integer or double-precision float type are safe
         * for storing this identifier.
         */
        val migrate_to_chat_id: Long? = null,
        /**
         * Optional. The supergroup has been migrated from a group with the
         * specified identifier. This number may have more than 32 significant
         * bits and some programming languages may have difficulty/silent
         * defects in interpreting it. But it has at most 52 significant bits,
         * so a signed 64-bit integer or double-precision float type are safe
         * for storing this identifier.
         */
        val migrate_from_chat_id: Long? = null,
        /**
         * Optional. Specified message was pinned. Note that the Message
         * object in this field will not contain further reply_to_message
         * fields even if it is itself a reply.
         */
        val pinned_message: Message? = null,
        /**
         * Optional. Message is an invoice for a payment,
         * information about the invoice.
         */
        val invoice: Invoice? = null,
        /**
         * Optional. Message is a service message about a successful payment,
         * information about the payment.
         */
        val successful_payment: SuccessfulPayment? = null,
        /**
         * Optional. The domain name of the website on which the user has logged in.
         */
        val connected_website: String? = null,
        /**
         * Optional. Telegram Passport data
         */
        val passport_data: PassportData? = null,
        /**
         * Optional. Service message. A user in the chat triggered another
         * user's proximity alert while sharing Live Location.
         */
        val proximity_alert_triggered: ProximityAlertTriggered? = null,
        /**
         * Optional. Service message: voice chat scheduled
         */
        val voice_chat_scheduled: VoiceChatScheduled? = null,
        /**
         * Optional. Service message: voice chat started
         */
        val voice_chat_started: VoiceChatStarted? = null,
        /**
         * Optional. Service message: voice chat ended
         */
        val voice_chat_ended: VoiceChatEnded? = null,
        /**
         * Optional. Service message: new participants invited to a voice chat
         */
        val voice_chat_participants_invited: VoiceChatParticipantsInvited? = null,
        /**
         * Optional. Inline keyboard attached to the message. `login_url`
         * buttons are represented as ordinary `url` buttons.
         */
        val reply_markup: InlineKeyboardMarkup? = null,
    )
    @Serializable
    public data class MessageId(
        /**
         * Unique message identifier
         */
        val message_id: Int
    )
    @Serializable
    public class InlineQuery()
    @Serializable
    public class ChosenInlineResult()
    @Serializable
    public class CallbackQuery()
    @Serializable
    public class ShippingQuery()
    @Serializable
    public class PreCheckoutQuery()
    @Serializable
    public class Poll()
    @Serializable
    public class PollAnswer()
    @Serializable
    public class ChatMemberUpdated()
    @Serializable
    public class Chat(
        /**
         * Unique identifier for this chat. This number may have more than 32
         * significant bits and some programming languages may have
         * difficulty/silent defects in interpreting it. But it has at most
         * 52 significant bits, so a signed 64-bit integer or double-precision
         * float type are safe for storing this identifier.
         */
        public val id: Long,
        /**
         * Type of chat, can be either “private”, “group”, “supergroup” or “channel”
         */
        public val type: String,
    )
    @Serializable
    public class MessageEntity()
    @Serializable
    public class Animation()
    @Serializable
    public class Audio()
    @Serializable
    public class Document()
    @Serializable
    public class PhotoSize()
    @Serializable
    public class Sticker()
    @Serializable
    public class Video()
    @Serializable
    public class VideoNote()
    @Serializable
    public class Voice()
    @Serializable
    public data class Contact(
        val phone_number: String,
        val first_name: String,
        val last_name: String?,
        val user_id: Long?,
        val vcard: String?,
    )
    @Serializable
    public class Dice()
    @Serializable
    public class Game()
    @Serializable
    public class Venue()
    @Serializable
    public class Location()
    @Serializable
    public class MessageAutoDeleteTimerChanged()
    @Serializable
    public class Invoice()
    @Serializable
    public class SuccessfulPayment()
    @Serializable
    public class PassportData()
    @Serializable
    public class ProximityAlertTriggered()
    @Serializable
    public class VoiceChatScheduled()
    @Serializable
    public class VoiceChatStarted()
    @Serializable
    public class VoiceChatEnded()
    @Serializable
    public class VoiceChatParticipantsInvited()
    @Serializable
    public class InlineKeyboardMarkup()
}

/**
 * This object represents a Telegram user or bot.
 */
@Serializable
public data class User(
    /**
     * Unique identifier for this user or bot
     */
    public val id: Long,

    /**
     * True, if this user is a bot
     */
    public val is_bot: Boolean,

    /**
     * User's or bot's first name
     */
    public val first_name: String,

    /**
     * Optional. User's or bot's last name
     */
    public val last_name: String? = null,

    /**
     * Optional. User's or bot's username
     */
    public val username: String? = null,

    /**
     * Optional. IETF language tag of the user's language
     */
    public val language_code: String? = null,

    /**
     * Optional. True, if the bot can be invited to groups.
     * Returned only in getMe.
     */
    public val can_join_groups: Boolean? = null,

    /**
     * Optional. True, if privacy mode is disabled for the bot.
     * Returned only in getMe.
     */
    public val can_read_all_group_messages: Boolean? = null,

    /**
     * Optional. True, if the bot supports inline queries.
     * Returned only in getMe.
     */
    public val supports_inline_queries: Boolean? = null,
)
