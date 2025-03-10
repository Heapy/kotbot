package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlinx.serialization.Serializable

/**
 * This [object](https://core.telegram.org/bots/api/#available-types) represents an incoming update.  
 * At most **one** of the optional parameters can be present in any given update.
 */
@Serializable
public data class Update(
    /**
     * The update's unique identifier. Update identifiers start from a certain positive number and increase sequentially. This identifier becomes especially handy if you're using [webhooks](https://core.telegram.org/bots/api/#setwebhook), since it allows you to ignore repeated updates or to restore the correct update sequence, should they get out of order. If there are no new updates for at least a week, then identifier of the next update will be chosen randomly instead of sequentially.
     */
    public val update_id: Int,
    /**
     * *Optional*. New incoming message of any kind - text, photo, sticker, etc.
     */
    public val message: Message? = null,
    /**
     * *Optional*. New version of a message that is known to the bot and was edited. This update may at times be triggered by changes to message fields that are either unavailable or not actively used by your bot.
     */
    public val edited_message: Message? = null,
    /**
     * *Optional*. New incoming channel post of any kind - text, photo, sticker, etc.
     */
    public val channel_post: Message? = null,
    /**
     * *Optional*. New version of a channel post that is known to the bot and was edited. This update may at times be triggered by changes to message fields that are either unavailable or not actively used by your bot.
     */
    public val edited_channel_post: Message? = null,
    /**
     * *Optional*. The bot was connected to or disconnected from a business account, or a user edited an existing connection with the bot
     */
    public val business_connection: BusinessConnection? = null,
    /**
     * *Optional*. New message from a connected business account
     */
    public val business_message: Message? = null,
    /**
     * *Optional*. New version of a message from a connected business account
     */
    public val edited_business_message: Message? = null,
    /**
     * *Optional*. Messages were deleted from a connected business account
     */
    public val deleted_business_messages: BusinessMessagesDeleted? = null,
    /**
     * *Optional*. A reaction to a message was changed by a user. The bot must be an administrator in the chat and must explicitly specify `"message_reaction"` in the list of *allowed_updates* to receive these updates. The update isn't received for reactions set by bots.
     */
    public val message_reaction: MessageReactionUpdated? = null,
    /**
     * *Optional*. Reactions to a message with anonymous reactions were changed. The bot must be an administrator in the chat and must explicitly specify `"message_reaction_count"` in the list of *allowed_updates* to receive these updates. The updates are grouped and can be sent with delay up to a few minutes.
     */
    public val message_reaction_count: MessageReactionCountUpdated? = null,
    /**
     * *Optional*. New incoming [inline](https://core.telegram.org/bots/api/#inline-mode) query
     */
    public val inline_query: InlineQuery? = null,
    /**
     * *Optional*. The result of an [inline](https://core.telegram.org/bots/api/#inline-mode) query that was chosen by a user and sent to their chat partner. Please see our documentation on the [feedback collecting](https://core.telegram.org/bots/inline#collecting-feedback) for details on how to enable these updates for your bot.
     */
    public val chosen_inline_result: ChosenInlineResult? = null,
    /**
     * *Optional*. New incoming callback query
     */
    public val callback_query: CallbackQuery? = null,
    /**
     * *Optional*. New incoming shipping query. Only for invoices with flexible price
     */
    public val shipping_query: ShippingQuery? = null,
    /**
     * *Optional*. New incoming pre-checkout query. Contains full information about checkout
     */
    public val pre_checkout_query: PreCheckoutQuery? = null,
    /**
     * *Optional*. A user purchased paid media with a non-empty payload sent by the bot in a non-channel chat
     */
    public val purchased_paid_media: PaidMediaPurchased? = null,
    /**
     * *Optional*. New poll state. Bots receive only updates about manually stopped polls and polls, which are sent by the bot
     */
    public val poll: Poll? = null,
    /**
     * *Optional*. A user changed their answer in a non-anonymous poll. Bots receive new votes only in polls that were sent by the bot itself.
     */
    public val poll_answer: PollAnswer? = null,
    /**
     * *Optional*. The bot's chat member status was updated in a chat. For private chats, this update is received only when the bot is blocked or unblocked by the user.
     */
    public val my_chat_member: ChatMemberUpdated? = null,
    /**
     * *Optional*. A chat member's status was updated in a chat. The bot must be an administrator in the chat and must explicitly specify `"chat_member"` in the list of *allowed_updates* to receive these updates.
     */
    public val chat_member: ChatMemberUpdated? = null,
    /**
     * *Optional*. A request to join the chat has been sent. The bot must have the *can_invite_users* administrator right in the chat to receive these updates.
     */
    public val chat_join_request: ChatJoinRequest? = null,
    /**
     * *Optional*. A chat boost was added or changed. The bot must be an administrator in the chat to receive these updates.
     */
    public val chat_boost: ChatBoostUpdated? = null,
    /**
     * *Optional*. A boost was removed from a chat. The bot must be an administrator in the chat to receive these updates.
     */
    public val removed_chat_boost: ChatBoostRemoved? = null,
)
