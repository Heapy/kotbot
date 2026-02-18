package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.model.Message
import kotlinx.serialization.Serializable

@Serializable
sealed interface KotbotCallBackData

@Serializable
data class BanUserInReplyCallbackData(
    val message: Message,
    val reason: String,
) : KotbotCallBackData

@Serializable
data class StrikeUserInReplyCallbackData(
    val message: Message,
    val reason: String,
) : KotbotCallBackData


@Serializable
data class SendGptMessageCallbackData(
    val groupChatId: Long,
    val waitMessageId: Int,
    val responseText: String,
    val sessionId: Long,
) : KotbotCallBackData

@Serializable
data class DismissGptCallbackData(
    val groupChatId: Long,
    val waitMessageId: Int,
    val sessionId: Long,
) : KotbotCallBackData

@Serializable
data class DeleteMessageInReplyCallbackData(
    val message: Message,
    val reason: String,
) : KotbotCallBackData

@Serializable
data class DismissCallbackData(
    val message: Message,
) : KotbotCallBackData
