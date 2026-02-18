package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.model.Update
import io.heapy.kotbot.bot.model.Poll
import io.heapy.kotbot.bot.model.PollAnswer
import io.heapy.kotbot.bot.model.PreCheckoutQuery
import io.heapy.kotbot.bot.model.BusinessConnection
import io.heapy.kotbot.bot.model.BusinessMessagesDeleted
import io.heapy.kotbot.bot.model.CallbackQuery
import io.heapy.kotbot.bot.model.ChatBoostRemoved
import io.heapy.kotbot.bot.model.ChatBoostUpdated
import io.heapy.kotbot.bot.model.ChatJoinRequest
import io.heapy.kotbot.bot.model.ChatMemberUpdated
import io.heapy.kotbot.bot.model.ChosenInlineResult
import io.heapy.kotbot.bot.model.InlineQuery
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.MessageReactionCountUpdated
import io.heapy.kotbot.bot.model.MessageReactionUpdated
import io.heapy.kotbot.bot.model.PaidMediaPurchased
import io.heapy.kotbot.bot.model.ShippingQuery
import kotlinx.serialization.Serializable

@Serializable
sealed interface TypedUpdate {
    val updateId: Int
}

@Serializable
data class TypedMessage(
    override val updateId: Int,
    val value: Message,
) : TypedUpdate

@Serializable
data class TypedEditedMessage(
    override val updateId: Int,
    val value: Message,
) : TypedUpdate

@Serializable
data class TypedChannelPost(
    override val updateId: Int,
    val value: Message,
) : TypedUpdate

@Serializable
data class TypedEditedChannelPost(
    override val updateId: Int,
    val value: Message,
) : TypedUpdate

@Serializable
data class TypedBusinessConnection(
    override val updateId: Int,
    val value: BusinessConnection,
) : TypedUpdate

@Serializable
data class TypedBusinessMessage(
    override val updateId: Int,
    val value: Message,
) : TypedUpdate

@Serializable
data class TypedEditedBusinessMessage(
    override val updateId: Int,
    val value: Message,
) : TypedUpdate

@Serializable
data class TypedDeletedBusinessMessages(
    override val updateId: Int,
    val value: BusinessMessagesDeleted,
) : TypedUpdate

@Serializable
data class TypedMessageReaction(
    override val updateId: Int,
    val value: MessageReactionUpdated,
) : TypedUpdate

@Serializable
data class TypedMessageReactionCount(
    override val updateId: Int,
    val value: MessageReactionCountUpdated,
) : TypedUpdate

@Serializable
data class TypedInlineQuery(
    override val updateId: Int,
    val value: InlineQuery,
) : TypedUpdate

@Serializable
data class TypedChosenInlineResult(
    override val updateId: Int,
    val value: ChosenInlineResult,
) : TypedUpdate

@Serializable
data class TypedCallbackQuery(
    override val updateId: Int,
    val value: CallbackQuery,
) : TypedUpdate

@Serializable
data class TypedShippingQuery(
    override val updateId: Int,
    val value: ShippingQuery,
) : TypedUpdate

@Serializable
data class TypedPreCheckoutQuery(
    override val updateId: Int,
    val value: PreCheckoutQuery,
) : TypedUpdate

@Serializable
data class TypedPurchasedPaidMedia(
    override val updateId: Int,
    val value: PaidMediaPurchased,
) : TypedUpdate

@Serializable
data class TypedPoll(
    override val updateId: Int,
    val value: Poll,
) : TypedUpdate

@Serializable
data class TypedPollAnswer(
    override val updateId: Int,
    val value: PollAnswer,
) : TypedUpdate

@Serializable
data class TypedMyChatMember(
    override val updateId: Int,
    val value: ChatMemberUpdated,
) : TypedUpdate

@Serializable
data class TypedChatMember(
    override val updateId: Int,
    val value: ChatMemberUpdated,
) : TypedUpdate

@Serializable
data class TypedChatJoinRequest(
    override val updateId: Int,
    val value: ChatJoinRequest,
) : TypedUpdate

@Serializable
data class TypedChatBoost(
    override val updateId: Int,
    val value: ChatBoostUpdated,
) : TypedUpdate

@Serializable
data class TypedRemovedChatBoost(
    override val updateId: Int,
    val value: ChatBoostRemoved,
) : TypedUpdate

/**
 * Convert [Update] to sealed interface
 */
fun Update.toTypedUpdate(): TypedUpdate {
    val message = message
    val edited_message = edited_message
    val channel_post = channel_post
    val edited_channel_post = edited_channel_post
    val business_connection = business_connection
    val business_message = business_message
    val edited_business_message = edited_business_message
    val deleted_business_messages = deleted_business_messages
    val message_reaction = message_reaction
    val message_reaction_count = message_reaction_count
    val inline_query = inline_query
    val chosen_inline_result = chosen_inline_result
    val callback_query = callback_query
    val shipping_query = shipping_query
    val pre_checkout_query = pre_checkout_query
    val purchased_paid_media = purchased_paid_media
    val poll = poll
    val poll_answer = poll_answer
    val my_chat_member = my_chat_member
    val chat_member = chat_member
    val chat_join_request = chat_join_request
    val chat_boost = chat_boost
    val removed_chat_boost = removed_chat_boost

    return when {
        message != null -> TypedMessage(update_id, message)
        edited_message != null -> TypedEditedMessage(update_id, edited_message)
        channel_post != null -> TypedChannelPost(update_id, channel_post)
        edited_channel_post != null -> TypedEditedChannelPost(update_id, edited_channel_post)
        business_connection != null -> TypedBusinessConnection(update_id, business_connection)
        business_message != null -> TypedBusinessMessage(update_id, business_message)
        edited_business_message != null -> TypedEditedBusinessMessage(update_id, edited_business_message)
        deleted_business_messages != null -> TypedDeletedBusinessMessages(update_id, deleted_business_messages)
        message_reaction != null -> TypedMessageReaction(update_id, message_reaction)
        message_reaction_count != null -> TypedMessageReactionCount(update_id, message_reaction_count)
        inline_query != null -> TypedInlineQuery(update_id, inline_query)
        chosen_inline_result != null -> TypedChosenInlineResult(update_id, chosen_inline_result)
        callback_query != null -> TypedCallbackQuery(update_id, callback_query)
        shipping_query != null -> TypedShippingQuery(update_id, shipping_query)
        pre_checkout_query != null -> TypedPreCheckoutQuery(update_id, pre_checkout_query)
        purchased_paid_media != null -> TypedPurchasedPaidMedia(update_id, purchased_paid_media)
        poll != null -> TypedPoll(update_id, poll)
        poll_answer != null -> TypedPollAnswer(update_id, poll_answer)
        my_chat_member != null -> TypedMyChatMember(update_id, my_chat_member)
        chat_member != null -> TypedChatMember(update_id, chat_member)
        chat_join_request != null -> TypedChatJoinRequest(update_id, chat_join_request)
        chat_boost != null -> TypedChatBoost(update_id, chat_boost)
        removed_chat_boost != null -> TypedRemovedChatBoost(update_id, removed_chat_boost)
        else -> error("Update type not supported")
    }
}
