package io.heapy.kotbot.bot

import com.fasterxml.jackson.annotation.JsonProperty
import org.telegram.telegrambots.meta.api.objects.Update

public sealed class TelegramUpdate {
    /**
     * The update's unique identifier. Update identifiers start from a certain
     * positive number and increase sequentially. This ID becomes especially
     * handy if you're using Webhooks, since it allows you to ignore repeated
     * updates or to restore the correct update sequence, should they get out of
     * order. If there are no new updates for at least a week, then identifier
     * of the next update will be chosen randomly instead of sequentially.
     */
    public abstract val updateId: Int
}

internal fun Update.toTelegramUpdate(): List<TelegramUpdate> {
    @OptIn(ExperimentalStdlibApi::class)
    return buildList {
        if (hasEditedMessage()) add(EditedMessage(this@toTelegramUpdate))
        if (hasChannelPost()) add(ChannelPost(this@toTelegramUpdate))
        if (hasEditedChannelPost()) add(EditedChannelPost(this@toTelegramUpdate))
        if (hasInlineQuery()) add(InlineQuery(this@toTelegramUpdate))
        if (hasChosenInlineQuery()) add(ChosenInlineResult(this@toTelegramUpdate))
        if (hasCallbackQuery()) add(CallbackQuery(this@toTelegramUpdate))
        if (hasShippingQuery()) add(ShippingQuery(this@toTelegramUpdate))
        if (hasPreCheckoutQuery()) add(PreCheckoutQuery(this@toTelegramUpdate))
        if (hasPoll()) add(Poll(this@toTelegramUpdate))
        if (hasPollAnswer()) add(PollAnswer(this@toTelegramUpdate))
    }
}

/**
 * New incoming message of any kind — text, photo, sticker, etc.
 */
public data class Message(
    @JsonProperty("update_id")
    override val updateId: Int,
    @JsonProperty("message_id")
    val messageId: Int,
    val from: User
) : TelegramUpdate()

/**
 * New version of a message that is known to the bot and was edited
 */
public data class EditedMessage(
    @JsonProperty("update_id")
    override val updateId: Int,
    val messageId: Int
) : TelegramUpdate()

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
    @JsonProperty("update_id")
    override val updateId: Int,
    val messageId: Int
) : TelegramUpdate()

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
    @JsonProperty("update_id")
    override val updateId: Int,
    val messageId: Int
) : TelegramUpdate()

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
    @JsonProperty("update_id")
    override val updateId: Int,
    val messageId: Int
) : TelegramUpdate()

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
    @JsonProperty("update_id")
    override val updateId: Int,
    val messageId: Int
) : TelegramUpdate()

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
    @JsonProperty("update_id")
    override val updateId: Int,
    val messageId: Int
) : TelegramUpdate()

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
    @JsonProperty("update_id")
    override val updateId: Int,
    val messageId: Int
) : TelegramUpdate()

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
    @JsonProperty("update_id")
    override val updateId: Int,
    val messageId: Int
) : TelegramUpdate()

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
    @JsonProperty("update_id")
    override val updateId: Int,
    val messageId: Int
) : TelegramUpdate()

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
    @JsonProperty("update_id")
    override val updateId: Int,
    val messageId: Int
) : TelegramUpdate()

internal fun PollAnswer(update: Update): PollAnswer {
    return PollAnswer(
        updateId = update.updateId,
        messageId = update.message.messageId
    )
}

/**
 * This object represents a Telegram user or bot.
 */
public data class User(
    /**
     * Unique identifier for this user or bot
     */
    public val id: Int,

    /**
     * True, if this user is a bot
     */
    @JsonProperty("is_bot")
    public val isBot: Boolean,

    /**
     * User's or bot's first name
     */
    @JsonProperty("first_name")
    public val firstName: String,

    /**
     * Optional. User's or bot's last name
     */
    @JsonProperty("last_name")
    public val lastName: String?,

    /**
     * Optional. User's or bot's username
     */
    public val username: String?,

    /**
     * Optional. IETF language tag of the user's language
     */
    @JsonProperty("language_code")
    public val languageCode: String?,

    /**
     * Optional. True, if the bot can be invited to groups.
     * Returned only in getMe.
     */
    @JsonProperty("can_join_groups")
    val canJoinGroups: Boolean?,
    /**
     * Optional. True, if privacy mode is disabled for the bot.
     * Returned only in getMe.
     */
    @JsonProperty("can_read_all_group_messages")
    val canReadAllGroupMessages: Boolean?,
    /**
     * Optional. True, if the bot supports inline queries.
     * Returned only in getMe.
     */
    @JsonProperty("supports_inline_queries")
    val supportsInlineQueries: Boolean?,
)
