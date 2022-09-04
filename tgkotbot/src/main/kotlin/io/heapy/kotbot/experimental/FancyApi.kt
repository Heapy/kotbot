package io.heapy.kotbot.experimental

import io.heapy.kotbot.bot.model.Update
import io.heapy.kotbot.bot.model.User

// Attempt to provide convenient access to the bot API.

sealed interface TelegramUpdate {
    /**
     * The update's unique identifier. Update identifiers start from a certain
     * positive number and increase sequentially. This ID becomes especially
     * handy if you're using Webhooks, since it allows you to ignore repeated
     * updates or to restore the correct update sequence, should they get out of
     * order. If there are no new updates for at least a week, then identifier
     * of the next update will be chosen randomly instead of sequentially.
     */
    val updateId: Int
}

internal fun Update.toTelegramUpdate(): List<TelegramUpdate> {
    val update = this
    return buildList {
        update.edited_message?.let { add(EditedMessage(update)) }
        update.channel_post?.let { add(ChannelPost(update)) }
        update.edited_channel_post?.let { add(EditedChannelPost(update)) }
        update.inline_query?.let { add(InlineQuery(update)) }
        update.chosen_inline_result?.let { add(ChosenInlineResult(update)) }
        update.callback_query?.let { add(CallbackQuery(update)) }
        update.shipping_query?.let { add(ShippingQuery(update)) }
        update.pre_checkout_query?.let { add(PreCheckoutQuery(update)) }
        update.poll?.let { add(Poll(update)) }
        update.poll_answer?.let { add(PollAnswer(update)) }
    }
}

/**
 * New incoming message of any kind — text, photo, sticker, etc.
 */
data class Message(
    override val updateId: Int,
    val messageId: Int,
    val from: User
) : TelegramUpdate

/**
 * New version of a message that is known to the bot and was edited
 */
data class EditedMessage(
    override val updateId: Int,
    val messageId: Int
) : TelegramUpdate

internal fun EditedMessage(update: Update): EditedMessage {
    return EditedMessage(
        updateId = update.update_id,
        messageId = update.message!!.message_id
    )
}

/**
 * New incoming channel post of any kind — text, photo, sticker, etc.
 */
data class ChannelPost(
    override val updateId: Int,
    val messageId: Int
) : TelegramUpdate

internal fun ChannelPost(update: Update): ChannelPost {
    return ChannelPost(
        updateId = update.update_id,
        messageId = update.message!!.message_id
    )
}

/**
 * New version of a channel post that is known to the bot and was edited
 */
data class EditedChannelPost(
    override val updateId: Int,
    val messageId: Int
) : TelegramUpdate

internal fun EditedChannelPost(update: Update): EditedChannelPost {
    return EditedChannelPost(
        updateId = update.update_id,
        messageId = update.message!!.message_id
    )
}

/**
 * New incoming inline query
 */
data class InlineQuery(
    override val updateId: Int,
    val messageId: Int
) : TelegramUpdate

internal fun InlineQuery(update: Update): InlineQuery {
    return InlineQuery(
        updateId = update.update_id,
        messageId = update.message!!.message_id
    )
}

/**
 * The result of an inline query that was chosen by a user and sent to their
 * chat partner. Please see our documentation on the feedback collecting for
 * details on how to enable these updates for your bot.
 */
data class ChosenInlineResult(
    override val updateId: Int,
    val messageId: Int
) : TelegramUpdate

internal fun ChosenInlineResult(update: Update): ChosenInlineResult {
    return ChosenInlineResult(
        updateId = update.update_id,
        messageId = update.message!!.message_id
    )
}

/**
 * New incoming callback query
 */
data class CallbackQuery(
    override val updateId: Int,
    val messageId: Int
) : TelegramUpdate

internal fun CallbackQuery(update: Update): CallbackQuery {
    return CallbackQuery(
        updateId = update.update_id,
        messageId = update.message!!.message_id
    )
}

/**
 * New incoming shipping query. Only for invoices with flexible price
 */
data class ShippingQuery(
    override val updateId: Int,
    val messageId: Int
) : TelegramUpdate

internal fun ShippingQuery(update: Update): ShippingQuery {
    return ShippingQuery(
        updateId = update.update_id,
        messageId = update.message!!.message_id
    )
}

/**
 * New incoming pre-checkout query. Contains full information about checkout
 */
data class PreCheckoutQuery(
    override val updateId: Int,
    val messageId: Int
) : TelegramUpdate

internal fun PreCheckoutQuery(update: Update): PreCheckoutQuery {
    return PreCheckoutQuery(
        updateId = update.update_id,
        messageId = update.message!!.message_id
    )
}

/**
 * New poll state. Bots receive only updates about stopped polls and polls,
 * which are sent by the bot
 */
data class Poll(
    override val updateId: Int,
    val messageId: Int
) : TelegramUpdate

internal fun Poll(update: Update): Poll {
    return Poll(
        updateId = update.update_id,
        messageId = update.message!!.message_id
    )
}

/**
 * A user changed their answer in a non-anonymous poll. Bots receive new votes
 * only in polls that were sent by the bot itself.
 */
data class PollAnswer(
    override val updateId: Int,
    val messageId: Int
) : TelegramUpdate

internal fun PollAnswer(update: Update): PollAnswer {
    return PollAnswer(
        updateId = update.update_id,
        messageId = update.message!!.message_id
    )
}
