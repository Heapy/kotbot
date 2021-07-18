package io.heapy.kotbot.bot

import kotlinx.serialization.Serializable
import org.telegram.telegrambots.meta.api.objects.Update

@Serializable
public data class ApiUpdate(
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
    val message: Message?,
    /**
     * Optional. New version of a message that is known to the bot and was edited
     */
    val edited_message: Message?,
    /**
     * Optional. New incoming channel post of any kind — text, photo, sticker, etc.
     */
    val channel_post: Message?,
    /**
     * Optional. New version of a channel post that is known to the bot and was edited
     */
    val edited_channel_post: Message?,
    /**
     * Optional. New incoming inline query
     */
    val inline_query: Message?,
    /**
     * Optional. The result of an inline query that was chosen by a user and
     * sent to their chat partner. Please see our documentation on the feedback
     * collecting for details on how to enable these updates for your bot.
     */
    val chosen_inline_result: ChosenInlineResult?,
    /**
     * Optional. New incoming callback query
     */
    val callback_query: CallbackQuery?,
    /**
     * Optional. New incoming shipping query. Only for invoices with flexible price
     */
    val shipping_query: ShippingQuery?,
    /**
     * Optional. New incoming pre-checkout query. Contains full information about checkout
     */
    val pre_checkout_query: PreCheckoutQuery?,
    /**
     * Optional. New poll state. Bots receive only updates about stopped polls
     * and polls, which are sent by the bot
     */
    val poll: Poll?,
    /**
     * Optional. A user changed their answer in a non-anonymous poll. Bots
     * receive new votes only in polls that were sent by the bot itself.
     */
    val poll_answer: PollAnswer?,
    /**
     * Optional. The bot's chat member status was updated in a chat. For private
     * chats, this update is received only when the bot is blocked or unblocked by the user.
     */
    val my_chat_member: ChatMemberUpdated?,
    /**
     * Optional. A chat member's status was updated in a chat. The bot must be
     * an administrator in the chat and must explicitly specify “chat_member”
     * in the list of allowed_updates to receive these updates.
     */
    val chat_member: ChatMemberUpdated?,
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
        val from: User?,
        /**
         * Optional. Sender of the message, sent on behalf of a chat. The
         * channel itself for channel messages. The supergroup itself for
         * messages from anonymous group administrators. The linked channel
         * for messages automatically forwarded to the discussion group
         */
        val sender_chat: Chat?,
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
        val forward_from: User?,
        /**
         * Optional. For messages forwarded from channels or from anonymous
         * administrators, information about the original sender chat
         */
        val forward_from_chat: Chat?,
        /**
         * Optional. For messages forwarded from channels, identifier of the
         * original message in the channel
         */
        val forward_from_message_id: Int?,
        /**
         * Optional. For messages forwarded from channels, signature of
         * the post author if present
         */
        val forward_signature: String?,
        /**
         * Optional. Sender's name for messages forwarded from users who disallow
         * adding a link to their account in forwarded messages
         */
        val forward_sender_name: String?,
        /**
         * Optional. For forwarded messages, date the original message was
         * sent in Unix time
         */
        val forward_date: Int?,
        /**
         * Optional. For replies, the original message. Note that the Message
         * object in this field will not contain further reply_to_message
         * fields even if it itself is a reply.
         */
        val reply_to_message: Message?,
        /**
         * Optional. Bot through which the message was sent
         */
        val via_bot: User?,
        /**
         * Optional. Date the message was last edited in Unix time
         */
        val edit_date: Int?,
        /**
         * Optional. The unique identifier of a media message group
         * this message belongs to
         */
        val media_group_id: String?,
        /**
         * Optional. Signature of the post author for messages in channels, or
         * the custom title of an anonymous group administrator
         */
        val author_signature: String?,
        /**
         * Optional. For text messages, the actual UTF-8 text of the message,
         * 0-4096 characters
         */
        val text: String?,
        /**
         * Optional. For text messages, special entities like usernames, URLs,
         * bot commands, etc. that appear in the text
         */
        val entities: List<MessageEntity>?,
        /**
         * Optional. Message is an animation, information about the animation.
         * For backward compatibility, when this field is set,
         * the document field will also be set
         */
        val animation: Animation?,
        /**
         * Optional. Message is an audio file, information about the file
         */
        val audio: Audio?,
        /**
         * Optional. Message is a general file, information about the file
         */
        val document: Document?,
        /**
         * Optional. Message is a photo, available sizes of the photo
         */
        val photo: List<PhotoSize>?,
        /**
         * Optional. Message is a sticker, information about the sticker
         */
        val sticker: Sticker?,
        /**
         * Optional. Message is a video, information about the video
         */
        val video: Video?,
        /**
         * Optional. Message is a video note, information about the video message
         */
        val video_note: VideoNote?,
        /**
         * Optional. Message is a voice message, information about the file
         */
        val voice: Voice?,
        /**
         * Optional. Caption for the animation, audio, document, photo,
         * video or voice, 0-1024 characters
         */
        val caption: String?,
        /**
         * Optional. For messages with a caption, special entities like usernames,
         * URLs, bot commands, etc. that appear in the caption
         */
        val caption_entities: List<MessageEntity>?,
        /**
         * Optional. Message is a shared contact, information about the contact
         */
        val contact: Contact?,
        /**
         * Optional. Message is a dice with random value
         */
        val dice: Dice?,
        /**
         * Optional. Message is a game, information about the game
         */
        val game: Game?,
        /**
         * Optional. Message is a native poll, information about the poll
         */
        val poll: Poll?,
        /**
         * Optional. Message is a venue, information about the venue. For
         * backward compatibility, when this field is set, the location
         * field will also be set
         */
        val venue: Venue?,
        /**
         * Optional. Message is a shared location, information about the location
         */
        val location: Location?,
        /**
         * Optional. New members that were added to the group or supergroup
         * and information about them (the bot itself may be one of these members)
         */
        val new_chat_members: List<User>?,
        /**
         * Optional. A member was removed from the group, information about
         * them (this member may be the bot itself)
         */
        val left_chat_member: List<User>?,
        /**
         * Optional. A chat title was changed to this value
         */
        val new_chat_title: String?,
        /**
         * Optional. A chat photo was change to this value
         */
        val new_chat_photo: List<PhotoSize>?,
        /**
         * Optional. Service message: the chat photo was deleted
         */
        val delete_chat_photo: Boolean?,
        /**
         * Optional. Service message: the group has been created
         */
        val group_chat_created: Boolean?,
        /**
         * Optional. Service message: the supergroup has been created. This
         * field can't be received in a message coming through updates, because
         * bot can't be a member of a supergroup when it is created. It can only
         * be found in reply_to_message if someone replies to a very first
         * message in a directly created supergroup.
         */
        val supergroup_chat_created: Boolean?,
        /**
         * Optional. Service message: the channel has been created.
         * This field can't be received in a message coming through updates,
         * because bot can't be a member of a channel when it is created.
         * It can only be found in reply_to_message if someone replies
         * to a very first message in a channel.
         */
        val channel_chat_created: Boolean?,
        /**
         * Optional. Service message: auto-delete timer settings changed in the chat
         */
        val message_auto_delete_timer_changed: MessageAutoDeleteTimerChanged?,
        /**
         * Optional. The group has been migrated to a supergroup with the
         * specified identifier. This number may have more than 32 significant
         * bits and some programming languages may have difficulty/silent
         * defects in interpreting it. But it has at most 52 significant bits,
         * so a signed 64-bit integer or double-precision float type are safe
         * for storing this identifier.
         */
        val migrate_to_chat_id: Long?,
        /**
         * Optional. The supergroup has been migrated from a group with the
         * specified identifier. This number may have more than 32 significant
         * bits and some programming languages may have difficulty/silent
         * defects in interpreting it. But it has at most 52 significant bits,
         * so a signed 64-bit integer or double-precision float type are safe
         * for storing this identifier.
         */
        val migrate_from_chat_id: Long?,
        /**
         * Optional. Specified message was pinned. Note that the Message
         * object in this field will not contain further reply_to_message
         * fields even if it is itself a reply.
         */
        val pinned_message: Message?,
        /**
         * Optional. Message is an invoice for a payment,
         * information about the invoice.
         */
        val invoice: Invoice?,
        /**
         * Optional. Message is a service message about a successful payment,
         * information about the payment.
         */
        val successful_payment: SuccessfulPayment?,
        /**
         * Optional. The domain name of the website on which the user has logged in.
         */
        val connected_website: String?,
        /**
         * Optional. Telegram Passport data
         */
        val passport_data: PassportData?,
        /**
         * Optional. Service message. A user in the chat triggered another
         * user's proximity alert while sharing Live Location.
         */
        val proximity_alert_triggered: ProximityAlertTriggered?,
        /**
         * Optional. Service message: voice chat scheduled
         */
        val voice_chat_scheduled: VoiceChatScheduled?,
        /**
         * Optional. Service message: voice chat started
         */
        val voice_chat_started: VoiceChatStarted?,
        /**
         * Optional. Service message: voice chat ended
         */
        val voice_chat_ended: VoiceChatEnded?,
        /**
         * Optional. Service message: new participants invited to a voice chat
         */
        val voice_chat_participants_invited: VoiceChatParticipantsInvited?,
        /**
         * Optional. Inline keyboard attached to the message. `login_url`
         * buttons are represented as ordinary `url` buttons.
         */
        val reply_markup: InlineKeyboardMarkup?,
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
    public class Chat()
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
    public class Contact()
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

public sealed interface TelegramUpdate {
    /**
     * The update's unique identifier. Update identifiers start from a certain
     * positive number and increase sequentially. This ID becomes especially
     * handy if you're using Webhooks, since it allows you to ignore repeated
     * updates or to restore the correct update sequence, should they get out of
     * order. If there are no new updates for at least a week, then identifier
     * of the next update will be chosen randomly instead of sequentially.
     */
    public val updateId: Int
}

internal fun Update.toTelegramUpdate(): List<TelegramUpdate> {
    val update = this
    @OptIn(ExperimentalStdlibApi::class)
    return buildList {
        if (hasEditedMessage()) add(EditedMessage(update))
        if (hasChannelPost()) add(ChannelPost(update))
        if (hasEditedChannelPost()) add(EditedChannelPost(update))
        if (hasInlineQuery()) add(InlineQuery(update))
        if (hasChosenInlineQuery()) add(ChosenInlineResult(update))
        if (hasCallbackQuery()) add(CallbackQuery(update))
        if (hasShippingQuery()) add(ShippingQuery(update))
        if (hasPreCheckoutQuery()) add(PreCheckoutQuery(update))
        if (hasPoll()) add(Poll(update))
        if (hasPollAnswer()) add(PollAnswer(update))
    }
}

/**
 * New incoming message of any kind — text, photo, sticker, etc.
 */
public data class Message(
    override val updateId: Int,
    val messageId: Int,
    val from: User
) : TelegramUpdate

/**
 * New version of a message that is known to the bot and was edited
 */
public data class EditedMessage(
    override val updateId: Int,
    val messageId: Int
) : TelegramUpdate

internal fun EditedMessage(update: Update): EditedMessage {
    return EditedMessage(
        updateId = update.updateId,
        messageId = update.message.messageId
    )
}

/**
 * New incoming channel post of any kind — text, photo, sticker, etc.
 */
public data class ChannelPost(
    override val updateId: Int,
    val messageId: Int
) : TelegramUpdate

internal fun ChannelPost(update: Update): ChannelPost {
    return ChannelPost(
        updateId = update.updateId,
        messageId = update.message.messageId
    )
}

/**
 * New version of a channel post that is known to the bot and was edited
 */
public data class EditedChannelPost(
    override val updateId: Int,
    val messageId: Int
) : TelegramUpdate

internal fun EditedChannelPost(update: Update): EditedChannelPost {
    return EditedChannelPost(
        updateId = update.updateId,
        messageId = update.message.messageId
    )
}

/**
 * New incoming inline query
 */
public data class InlineQuery(
    override val updateId: Int,
    val messageId: Int
) : TelegramUpdate

internal fun InlineQuery(update: Update): InlineQuery {
    return InlineQuery(
        updateId = update.updateId,
        messageId = update.message.messageId
    )
}

/**
 * The result of an inline query that was chosen by a user and sent to their
 * chat partner. Please see our documentation on the feedback collecting for
 * details on how to enable these updates for your bot.
 */
public data class ChosenInlineResult(
    override val updateId: Int,
    val messageId: Int
) : TelegramUpdate

internal fun ChosenInlineResult(update: Update): ChosenInlineResult {
    return ChosenInlineResult(
        updateId = update.updateId,
        messageId = update.message.messageId
    )
}

/**
 * New incoming callback query
 */
public data class CallbackQuery(
    override val updateId: Int,
    val messageId: Int
) : TelegramUpdate

internal fun CallbackQuery(update: Update): CallbackQuery {
    return CallbackQuery(
        updateId = update.updateId,
        messageId = update.message.messageId
    )
}

/**
 * New incoming shipping query. Only for invoices with flexible price
 */
public data class ShippingQuery(
    override val updateId: Int,
    val messageId: Int
) : TelegramUpdate

internal fun ShippingQuery(update: Update): ShippingQuery {
    return ShippingQuery(
        updateId = update.updateId,
        messageId = update.message.messageId
    )
}

/**
 * New incoming pre-checkout query. Contains full information about checkout
 */
public data class PreCheckoutQuery(
    override val updateId: Int,
    val messageId: Int
) : TelegramUpdate

internal fun PreCheckoutQuery(update: Update): PreCheckoutQuery {
    return PreCheckoutQuery(
        updateId = update.updateId,
        messageId = update.message.messageId
    )
}

/**
 * New poll state. Bots receive only updates about stopped polls and polls,
 * which are sent by the bot
 */
public data class Poll(
    override val updateId: Int,
    val messageId: Int
) : TelegramUpdate

internal fun Poll(update: Update): Poll {
    return Poll(
        updateId = update.updateId,
        messageId = update.message.messageId
    )
}

/**
 * A user changed their answer in a non-anonymous poll. Bots receive new votes
 * only in polls that were sent by the bot itself.
 */
public data class PollAnswer(
    override val updateId: Int,
    val messageId: Int
) : TelegramUpdate

internal fun PollAnswer(update: Update): PollAnswer {
    return PollAnswer(
        updateId = update.updateId,
        messageId = update.message.messageId
    )
}

/**
 * This object represents a Telegram user or bot.
 */
@Serializable
public data class User(
    /**
     * Unique identifier for this user or bot
     */
    public val id: Int,

    /**
     * True, if this user is a bot
     */
    public val isBot: Boolean,

    /**
     * User's or bot's first name
     */
    public val firstName: String,

    /**
     * Optional. User's or bot's last name
     */
    public val lastName: String?,

    /**
     * Optional. User's or bot's username
     */
    public val username: String?,

    /**
     * Optional. IETF language tag of the user's language
     */
    public val languageCode: String?,

    /**
     * Optional. True, if the bot can be invited to groups.
     * Returned only in getMe.
     */
    val canJoinGroups: Boolean?,
    /**
     * Optional. True, if privacy mode is disabled for the bot.
     * Returned only in getMe.
     */
    val canReadAllGroupMessages: Boolean?,
    /**
     * Optional. True, if the bot supports inline queries.
     * Returned only in getMe.
     */
    val supportsInlineQueries: Boolean?,
)
