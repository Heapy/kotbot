package io.heapy.kotbot.bot

import kotlinx.serialization.Serializable

/**
 * This [object](https://core.telegram.org/bots/api#available-types) represents an incoming update.
 * At most one of the optional parameters can be present in any given update.
 */
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
    /**
     * Optional. A request to join the chat has been sent. The bot must have
     * the can_invite_users administrator right in the chat to
     * receive these updates.
     */
    val chat_join_request: ChatJoinRequest? = null,
)

/**
 * This object represents a Telegram user or bot.
 */
@Serializable
public data class User(
    /**
     * Unique identifier for this user or bot. This number may have more than
     * 32 significant bits and some programming languages may have
     * difficulty/silent defects in interpreting it. But it has at most
     * 52 significant bits, so a 64-bit integer or double-precision float
     * type are safe for storing this identifier.
     */
    val id: Long,

    /**
     * True, if this user is a bot
     */
    val is_bot: Boolean,

    /**
     * User's or bot's first name
     */
    val first_name: String,

    /**
     * Optional. User's or bot's last name
     */
    val last_name: String? = null,

    /**
     * Optional. User's or bot's username
     */
    val username: String? = null,

    /**
     * Optional. IETF language tag of the user's language
     */
    val language_code: String? = null,

    /**
     * Optional. True, if the bot can be invited to groups.
     * Returned only in getMe.
     */
    val can_join_groups: Boolean? = null,

    /**
     * Optional. True, if privacy mode is disabled for the bot.
     * Returned only in getMe.
     */
    val can_read_all_group_messages: Boolean? = null,

    /**
     * Optional. True, if the bot supports inline queries.
     * Returned only in getMe.
     */
    val supports_inline_queries: Boolean? = null,
)

/**
 * This object represents a chat.
 */
@Serializable
public data class Chat(
    /**
     * Unique identifier for this chat. This number may have more than 32
     * significant bits and some programming languages may have
     * difficulty/silent defects in interpreting it. But it has at most
     * 52 significant bits, so a signed 64-bit integer or double-precision
     * float type are safe for storing this identifier.
     */
    val id: Long,
    /**
     * Type of chat, can be either “private”, “group”, “supergroup” or “channel”
     */
    val type: String,
    /**
     * Optional. Title, for supergroups, channels and group chats
     */
    val title: String? = null,
    /**
     * Optional. Username, for private chats, supergroups and channels if available
     */
    val username: String? = null,
    /**
     * Optional. First name of the other party in a private chat
     */
    val first_name: String? = null,
    /**
     * Optional. Last name of the other party in a private chat
     */
    val last_name: String? = null,
    /**
     * Optional. Chat photo.
     * Returned only in [getChat](https://core.telegram.org/bots/api#getchat).
     */
    val photo: ChatPhoto? = null,
    /**
     * Optional. Bio of the other party in a private chat.
     * Returned only in [getChat](https://core.telegram.org/bots/api#getchat).
     */
    val bio: String? = null,
    /**
     * Optional. True, if privacy settings of the other party in the private chat allows to use `tg://user?id=<user_id>` links only in chats with the user.
     * Returned only in [getChat](https://core.telegram.org/bots/api#getchat).
     */
    val has_private_forwards: Boolean = false,
    /**
     * Optional. Description, for groups, supergroups and channel chats. Returned only in [getChat](https://core.telegram.org/bots/api#getchat).
     */
    val description: String? = null,
    /**
     * Optional. Primary invite link, for groups, supergroups and channel chats.
     * Returned only in [getChat](https://core.telegram.org/bots/api#getchat).
     */
    val invite_link: String? = null,
    /**
     * Optional. The most recent pinned message (by sending date).
     * Returned only in [getChat](https://core.telegram.org/bots/api#getchat).
     */
    val pinned_message: Message? = null,
    /**
     * Optional. Default chat member permissions, for groups and supergroups.
     * Returned only in [getChat](https://core.telegram.org/bots/api#getchat).
     */
    val permissions: ChatPermissions? = null,
    /**
     * Optional. For supergroups, the minimum allowed delay between consecutive messages sent by each unpriviledged user; in seconds.
     * Returned only in [getChat](https://core.telegram.org/bots/api#getchat).
     */
    val slow_mode_delay: Int? = null,
    /**
     * Optional. The time after which all messages sent to the chat will be automatically deleted; in seconds.
     * Returned only in [getChat](https://core.telegram.org/bots/api#getchat).
     */
    val message_auto_delete_time: Int? = null,
    /**
     * Optional. True, if messages from the chat can't be forwarded to other chats.
     * Returned only in [getChat](https://core.telegram.org/bots/api#getchat).
     */
    val has_protected_content: Boolean? = null,
    /**
     * Optional. For supergroups, name of group sticker set.
     * Returned only in [getChat](https://core.telegram.org/bots/api#getchat).
     */
    val sticker_set_name: String? = null,
    /**
     * Optional. True, if the bot can change the group sticker set.
     * Returned only in [getChat](https://core.telegram.org/bots/api#getchat).
     */
    val can_set_sticker_set: Boolean? = false,
    /**
     * Optional. Unique identifier for the linked chat, i.e. the discussion group identifier for a channel and vice versa; for supergroups and channel chats. This identifier may be greater than 32 bits and some programming languages may have difficulty/silent defects in interpreting it. But it is smaller than 52 bits, so a signed 64 bit integer or double-precision float type are safe for storing this identifier.
     * Returned only in [getChat](https://core.telegram.org/bots/api#getchat).
     */
    val linked_chat_id: Int? = null,
    /**
     * Optional. For supergroups, the location to which the supergroup is connected.
     * Returned only in [getChat](https://core.telegram.org/bots/api#getchat).
     */
    val location: ChatLocation? = null,
)

/**
 * This object represents a message.
 */
@Serializable
public data class Message(
    /**
     * Unique message identifier inside this chat
     */
    val message_id: Int,
    /**
     * Optional. Sender of the message; empty for messages sent to channels.
     * For backward compatibility, the field contains a fake sender user in
     * non-channel chats, if the message was sent on behalf of a chat.
     */
    val from: User? = null,
    /**
     * Optional. Sender of the message, sent on behalf of a chat. For example,
     * the channel itself for channel posts, the supergroup itself for messages
     * from anonymous group administrators, the linked channel for messages
     * automatically forwarded to the discussion group. For backward
     * compatibility, the field from contains a fake sender user in non-channel
     * chats, if the message was sent on behalf of a chat.
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
     * Optional. True, if the message is a channel post that was automatically
     * forwarded to the connected discussion group
     */
    val is_automatic_forward: Boolean? = null,
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
     * Optional. True, if the message can't be forwarded
     */
    val has_protected_content: Boolean? = null,
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
    val left_chat_member: User? = null,
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

/**
 * This object represents a unique message identifier.
 */
@Serializable
public data class MessageId(
    /**
     * Unique message identifier
     */
    val message_id: Int,
)

/**
 * This object represents one special entity in a text message. For example, hashtags, usernames, URLs, etc.
 */
@Serializable
public data class MessageEntity(
    /**
     * Type of the entity. Currently, can be
     * “mention” (`@username`),
     * “hashtag” (`#hashtag`),
     * “cashtag” (`$USD`),
     * “bot_command” (`/start@jobs_bot`),
     * “url” (`https://telegram.org`),
     * “email” (`do-not-reply@telegram.org`),
     * “phone_number” (`+1-212-555-0123`),
     * “bold” (bold text),
     * “italic” (italic text),
     * “underline” (underlined text),
     * “strikethrough” (strikethrough text),
     * “spoiler” (spoiler message),
     * “code” (monowidth string),
     * “pre” (monowidth block),
     * “text_link” (for clickable text URLs),
     * “text_mention” (for users [without usernames](https://telegram.org/blog/edit#new-mentions))
     */
    val type: String,
    /**
     * Offset in UTF-16 code units to the start of the entity
     */
    val offset: Int,
    /**
     * Length of the entity in UTF-16 code units
     */
    val length: Int,
    /**
     * Optional. For “text_link” only, url that will be opened after user taps on the text
     */
    val url: String? = null,
    /**
     * Optional. For “text_mention” only, the mentioned user
     */
    val user: User? = null,
    /**
     * Optional. For “pre” only, the programming language of the entity text
     */
    val language: String? = null,
)

/**
 * This object represents one size of a photo or a file / sticker thumbnail.
 */
@Serializable
public data class PhotoSize(
    /**
     * Identifier for this file, which can be used to download or reuse the file
     */
    val file_id: String,
    /**
     * Unique identifier for this file, which is supposed to be the same over time and for different bots.
     * Can't be used to download or reuse the file.
     */
    val file_unique_id: String,
    /**
     * Photo width
     */
    val width: Int,
    /**
     * Photo height
     */
    val height: Int,
    /**
     * Optional. File size in bytes
     */
    val file_size: Int,
)

/**
 * This object represents an animation file
 * (GIF or H.264/MPEG-4 AVC video without sound).
 */
@Serializable
public data class Animation(
    /**
     * Identifier for this file, which can be used to download or reuse the file
     */
    val file_id: String,
    /**
     * Unique identifier for this file, which is supposed to be the same over time and for different bots.
     * Can't be used to download or reuse the file.
     */
    val file_unique_id: String,
    /**
     * Video width as defined by sender
     */
    val width: Int,
    /**
     * Video height as defined by sender
     */
    val height: Int,
    /**
     * Duration of the video in seconds as defined by sender
     */
    val duration: Int,
    /**
     * Optional. Animation thumbnail as defined by sender
     */
    val thumb: PhotoSize? = null,
    /**
     * Optional. Original animation filename as defined by sender
     */
    val file_name: String? = null,
    /**
     * Optional. MIME type of the file as defined by sender
     */
    val mime_type: String? = null,
    /**
     * Optional. File size in bytes
     */
    val file_size: Int? = null,
)

/**
 * This object represents an audio file to be treated as music by the Telegram clients.
 */
@Serializable
public data class Audio(
    /**
     * Identifier for this file, which can be used to download or reuse the file
     */
    val file_id: String,
    /**
     * Unique identifier for this file, which is supposed to be the same over time and for different bots.
     * Can't be used to download or reuse the file.
     */
    val file_unique_id: String,
    /**
     * Duration of the audio in seconds as defined by sender
     */
    val duration: Int,
    /**
     * Optional. Performer of the audio as defined by sender or by audio tags
     */
    val performer: String? = null,
    /**
     * Optional. Title of the audio as defined by sender or by audio tags
     */
    val title: String? = null,
    /**
     * Optional. Original filename as defined by sender
     */
    val file_name: String? = null,
    /**
     * Optional. MIME type of the file as defined by sender
     */
    val mime_type: String? = null,
    /**
     * Optional. File size in bytes
     */
    val file_size: Int? = null,
    /**
     * Optional. Thumbnail of the album cover to which the music file belongs
     */
    val thumb: PhotoSize? = null,
)

/**
 * This object represents a general file (as opposed to photos, voice messages and audio files).
 */
@Serializable
public data class Document(
    /**
     * Identifier for this file, which can be used to download or reuse the file
     */
    val file_id: String,
    /**
     * Identifier for this file, which can be used to download or reuse the file
     */
    val file_unique_id: String,
    /**
     * Optional. Document thumbnail as defined by sender
     */
    val thumb: PhotoSize? = null,
    /**
     * Optional. Original filename as defined by sender
     */
    val file_name: String? = null,
    /**
     * Optional. MIME type of the file as defined by sender
     */
    val mime_type: String? = null,
    /**
     * Optional. File size in bytes
     */
    val file_size: Int? = null,
)

/**
 * This object represents a video file.
 */
@Serializable
public data class Video(
    /**
     * Identifier for this file, which can be used to download or reuse the file
     */
    val file_id: String,
    /**
     * Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
     */
    val file_unique_id: String,
    /**
     * Video width as defined by sender
     */
    val width: Int,
    /**
     * Video height as defined by sender
     */
    val height: Int,
    /**
     * Duration of the video in seconds as defined by sender
     */
    val duration: Int,
    /**
     * Optional. Video thumbnail
     */
    val thumb: PhotoSize? = null,
    /**
     * Optional. Original filename as defined by sender
     */
    val file_name: String? = null,
    /**
     * Optional. MIME type of the file as defined by sender
     */
    val mime_type: String? = null,
    /**
     * Optional. File size in bytes
     */
    val file_size: Int? = null,
)

/**
 * This object represents a video message
 * (available in Telegram apps as of v.4.0).
 */
@Serializable
public data class VideoNote(
    /**
     * Identifier for this file, which can be used to download or reuse the file
     */
    val file_id: String,
    /**
     * Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
     */
    val file_unique_id: String,
    /**
     * Video width and height (diameter of the video message) as defined by sender
     */
    val length: Int,
    /**
     * Duration of the video in seconds as defined by sender
     */
    val duration: Int,
    /**
     * Optional. Video thumbnail
     */
    val thumb: PhotoSize? = null,
    /**
     * Optional. File size in bytes
     */
    val file_size: Int? = null,
)

/**
 * This object represents a voice note.
 */
@Serializable
public data class Voice(
    /**
     * Identifier for this file, which can be used to download or reuse the file
     */
    val file_id: String,
    /**
     * Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
     */
    val file_unique_id: String,
    /**
     * Duration of the audio in seconds as defined by sender
     */
    val duration: Int,
    /**
     * Optional. MIME type of the file as defined by sender
     */
    val mime_type: String? = null,
    /**
     * Optional. File size in bytes
     */
    val file_size: Int? = null,
)

/**
 * This object represents a phone contact.
 */
@Serializable
public data class Contact(
    /**
     * Contact's phone number
     */
    val phone_number: String,
    /**
     * Contact's first name
     */
    val first_name: String,
    /**
     * Optional. Contact's last name
     */
    val last_name: String? = null,
    /**
     * Optional. Contact's user identifier in Telegram. This number may have
     * more than 32 significant bits and some programming languages may have
     * difficulty/silent defects in interpreting it. But it has at most 52
     * significant bits, so a 64-bit integer or double-precision float type
     * are safe for storing this identifier.
     */
    val user_id: Long? = null,
    /**
     * Optional. Additional data about the contact in the form of a vCard
     */
    val vcard: String? = null,
)

/**
 * This object represents an animated emoji that displays a random value.
 */
@Serializable
public data class Dice(
    /**
     * Emoji on which the dice throw animation is based
     */
    val emoji: String,
    /**
     * Value of the dice, 1-6 for “🎲”, “🎯” and “🎳” base emoji, 1-5 for “🏀” and “⚽” base emoji, 1-64 for “🎰” base emoji
     */
    val value: Int,
)

/**
 * This object contains information about one answer option in a poll.
 */
@Serializable
public data class PollOption(
    /**
     * Option text, 1-100 characters
     */
    val text: String,
    /**
     * Number of users that voted for this option
     */
    val voter_count: Int,
)

/**
 * This object represents an answer of a user in a non-anonymous poll.
 */
@Serializable
public data class PollAnswer(
    /**
     * Unique poll identifier
     */
    val poll_id: String,
    /**
     * The user, who changed the answer to the poll
     */
    val user: User,
    /**
     * 0-based identifiers of answer options, chosen by the user.
     * May be empty if the user retracted their vote.
     */
    val option_ids: List<Int>,
)

/**
 * This object contains information about a poll.
 */
@Serializable
public data class Poll(
    /**
     * Unique poll identifier
     */
    val id: String,
    /**
     * Poll question, 1-300 characters
     */
    val question: String,
    /**
     * List of poll options
     */
    val options: List<PollOption>,
    /**
     * Total number of users that voted in the poll
     */
    val total_voter_count: Int,
    /**
     * True, if the poll is closed
     */
    val is_closed: Boolean,
    /**
     * True, if the poll is anonymous
     */
    val is_anonymous: Boolean,
    /**
     * Poll type, currently can be “regular” or “quiz”
     */
    val type: String,
    /**
     * True, if the poll allows multiple answers
     */
    val allows_multiple_answers: Boolean,
    /**
     * Optional. 0-based identifier of the correct answer option. Available
     * only for polls in the quiz mode, which are closed, or was sent
     * (not forwarded) by the bot or to the private chat with the bot.
     */
    val correct_option_id: Int? = null,
    /**
     * Optional. Text that is shown when a user chooses an incorrect answer
     * or taps on the lamp icon in a quiz-style poll, 0-200 characters
     */
    val explanation: String? = null,
    /**
     * Optional. Special entities like usernames, URLs, bot commands, etc.
     * that appear in the explanation
     */
    val explanation_entities: List<MessageEntity>? = null,
    /**
     * Optional. Amount of time in seconds
     * the poll will be active after creation
     */
    val open_period: Int? = null,
    /**
     * Optional. Point in time (Unix timestamp) when the
     * poll will be automatically closed
     */
    val close_date: Int? = null,
)

/**
 * This object represents a point on the map.
 */
@Serializable
public data class Location(
    /**
     * Longitude as defined by sender
     */
    val longitude: Double,
    /**
     * Latitude as defined by sender
     */
    val latitude: Double,
    /**
     * Optional. The radius of uncertainty for the location,
     * measured in meters; 0-1500
     */
    val horizontal_accuracy: Double? = null,
    /**
     * Optional. Time relative to the message sending date, during which the
     * location can be updated; in seconds. For active live locations only.
     */
    val live_period: Int? = null,
    /**
     * Optional. The direction in which user is moving, in degrees; 1-360.
     * For active live locations only.
     */
    val heading: Int? = null,
    /**
     * Optional. Maximum distance for proximity alerts about approaching another chat member,
     * in meters. For sent live locations only.
     */
    val proximity_alert_radius: Int? = null
)

/**
 * This object represents a venue.
 */
@Serializable
public data class Venue(
    /**
     * Venue location. Can't be a live location
     */
    val location: Location,
    /**
     * Name of the venue
     */
    val title: String,
    /**
     * Address of the venue
     */
    val address: String,
    /**
     * Optional. Foursquare identifier of the venue
     */
    val foursquare_id: String? = null,
    /**
     * Optional. Foursquare type of the venue.
     * (For example, “arts_entertainment/default”, “arts_entertainment/aquarium” or “food/icecream”.)
     */
    val foursquare_type: String? = null,
    /**
     * Optional. Google Places identifier of the venue
     */
    val google_place_id: String? = null,
    /**
     * Optional. Google Places type of the venue. (See [supported types](https://developers.google
     * .com/maps/documentation/places/web-service/supported_types).)
     */
    val google_place_type: String? = null,
)

/**
 * This object represents the content of a service message, sent whenever a user in the chat triggers a proximity alert set by another user.
 */
@Serializable
public data class ProximityAlertTriggered(
    /**
     * User that triggered the alert
     */
    val traveler: User,
    /**
     * User that set the alert
     */
    val watcher: User,
    /**
     * The distance between the users
     */
    val distance: Int,
)

/**
 * This object represents a service message about a change in auto-delete timer settings.
 */
@Serializable
public data class MessageAutoDeleteTimerChanged(
    /**
     * New auto-delete time for messages in the chat; in seconds
     */
    val message_auto_delete_time: Int,
)

/**
 * This object represents a service message about a voice chat scheduled in the chat.
 */
@Serializable
public data class VoiceChatScheduled(
    /**
     * Point in time (Unix timestamp) when the voice chat is supposed to be started by a chat administrator
     */
    val start_date: Int,
)

/**
 * This object represents a service message about a voice chat started in the chat.
 * Currently holds no information.
 */
@Serializable
public class VoiceChatStarted

/**
 * This object represents a service message about a voice chat ended in the chat.
 */
@Serializable
public data class VoiceChatEnded(
    /**
     * Voice chat duration in seconds
     */
    val duration: Int,
)

/**
 * This object represents a service message about new members invited to a voice chat.
 */
@Serializable
public data class VoiceChatParticipantsInvited(
    /**
     * Optional. New members that were invited to the voice chat
     */
    val users: List<User>? = null,
)

/**
 * This object represent a user's profile pictures.
 */
@Serializable
public data class UserProfilePhotos(
    /**
     * Total number of profile pictures the target user has
     */
    val total_count: Int,
    /**
     * Requested profile pictures (in up to 4 sizes each)
     */
    val photos: List<PhotoSize>,
)

/**
 * This object represents a file ready to be downloaded.
 * The file can be downloaded via the link `https://api.telegram.org/file/bot<token>/<file_path>`.
 * It is guaranteed that the link will be valid for at least 1 hour.
 * When the link expires, a new one can be requested by calling getFile.
 *
 * Maximum file size to download is 20 MB
 */
@Serializable
public data class File(
    /**
     * Identifier for this file, which can be used to download or reuse the file
     */
    val file_id: String,
    /**
     * Unique identifier for this file, which is supposed to be the same over time and for different bots.
     * Can't be used to download or reuse the file.
     */
    val file_unique_id: String,
    /**
     * Optional. File size in bytes, if known
     */
    val file_size: Int? = null,
    /**
     * Optional. File path. Use `https://api.telegram.org/file/bot<token>/<file_path>` to get the file.
     */
    val file_path: String? = null,
)

/**
 * This object represents a [custom keyboard](https://core.telegram.org/bots#keyboards)
 * with reply options (see [Introduction to bots](https://core.telegram.org/bots#keyboards)
 * for details and examples).
 */
@Serializable
public data class ReplyKeyboardMarkup(
    /**
     * Array of button rows, each represented by an Array of
     * [KeyboardButton] objects
     */
    val keyboard: List<List<KeyboardButton>>,
    /**
     * Optional. Requests clients to resize the keyboard vertically for optimal
     * fit (e.g., make the keyboard smaller if there are just two rows of
     * buttons). Defaults to false, in which case the custom keyboard is
     * always of the same height as the app's standard keyboard.
     */
    val resize_keyboard: Boolean? = null,
    /**
     * Optional. Requests clients to hide the keyboard as soon as it's been used.
     * The keyboard will still be available, but clients will automatically
     * display the usual letter-keyboard in the chat – the user can press a
     * special button in the input field to see the custom keyboard again.
     * Defaults to false.
     */
    val one_time_keyboard: Boolean? = null,
    /**
     * Optional. The placeholder to be shown in the input field when
     * the keyboard is active; 1-64 characters
     */
    val input_field_placeholder: String? = null,
    /**
     * Optional. Use this parameter if you want to show the keyboard to specific
     * users only. Targets: 1) users that are @mentioned in the text of the
     * [Message] object; 2) if the bot's message is a reply
     * (has reply_to_message_id), sender of the original message.
     *
     * Example: A user requests to change the bot's language, bot replies to
     * the request with a keyboard to select the new language.
     * Other users in the group don't see the keyboard.
     */
    val selective: Boolean? = null,
)

// TODO: https://core.telegram.org/bots/api#keyboardbutton

@Serializable
public class KeyboardButton

/**
 * Represents a location to which a chat is connected.
 */
@Serializable
public data class ChatLocation(
    /**
     * The location to which the supergroup is connected. Can't be a live location.
     */
    val location: Location,
    /**
     * Location address; 1-64 characters, as defined by the chat owner
     */
    val address: String,
)

/**
 * Describes actions that a non-administrator user is allowed to take in a chat.
 */
@Serializable
public data class ChatPermissions(
    /**
     * Optional. True, if the user is allowed to send text messages, contacts, locations and venues
     */
    val can_send_messages: Boolean? = null,
    // TODO https://core.telegram.org/bots/api#chatpermissions
)

/**
 * This object represents a chat photo.
 */
@Serializable
public data class ChatPhoto(
    /**
     * File identifier of small (160x160) chat photo. This file_id can be used
     * only for photo download and only for as long as the photo is not changed.
     */
    val small_file_id: String,
    /**
     * Unique file identifier of small (160x160) chat photo, which is supposed
     * to be the same over time and for different bots. Can't be used to
     * download or reuse the file.
     */
    val small_file_unique_id: String,
    /**
     * File identifier of big (640x640) chat photo. This file_id can be used
     * only for photo download and only for as long as the photo is not changed.
     */
    val big_file_id: String,
    /**
     * Unique file identifier of big (640x640) chat photo, which is supposed
     * to be the same over time and for different bots. Can't be used to
     * download or reuse the file.
     */
    val big_file_unique_id: String,
)

@Serializable
public class InlineQuery

@Serializable
public class ChosenInlineResult

@Serializable
public class CallbackQuery

@Serializable
public class ShippingQuery

@Serializable
public class PreCheckoutQuery

@Serializable
public class ChatMemberUpdated

@Serializable
public class ChatJoinRequest

@Serializable
public class Sticker

@Serializable
public class Game

@Serializable
public class Invoice

@Serializable
public class SuccessfulPayment

@Serializable
public class PassportData

@Serializable
public class InlineKeyboardMarkup

@Serializable
public class InputFile

/**
 * Contains information about the current status of a webhook.
 */
@Serializable
public data class WebhookInfo(
    /**
     * Webhook URL, may be empty if webhook is not set up
     */
    val url: String,
    /**
     * True, if a custom certificate was provided for webhook certificate checks
     */
    val has_custom_certificate: Boolean,
    /**
     * Number of updates awaiting delivery
     */
    val pending_update_count: Int,
    /**
     * Optional. Currently used webhook IP address
     */
    val ip_address: String? = null,
    /**
     * Optional. Unix time for the most recent error that happened when
     * trying to deliver an update via webhook
     */
    val last_error_date: Int? = null,
    /**
     * Optional. Error message in human-readable format for the most
     * recent error that happened when trying to deliver an update via webhook
     */
    val last_error_message: String? = null,
    /**
     * Optional. Maximum allowed number of simultaneous HTTPS connections
     * to the webhook for update delivery
     */
    val max_connections: Int? = null,
    /**
     * Optional. A list of update types the bot is subscribed to.
     * Defaults to all update types except chat_member
     */
    val allowed_updates: List<String>? = null,
)
