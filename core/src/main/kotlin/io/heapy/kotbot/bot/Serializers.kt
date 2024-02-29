package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.model.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

public object BotCommandScopeSerializer : JsonContentPolymorphicSerializer<BotCommandScope>(BotCommandScope::class) {
    override fun selectDeserializer(element: JsonElement): KSerializer<out BotCommandScope> =
        when (val type = element.jsonObject["type"]?.jsonPrimitive?.content) {
            "default" -> BotCommandScopeDefault.serializer()
            "all_private_chats" -> BotCommandScopeAllPrivateChats.serializer()
            "all_group_chats" -> BotCommandScopeAllGroupChats.serializer()
            "all_chat_administrators" -> BotCommandScopeAllChatAdministrators.serializer()
            "chat" -> BotCommandScopeChat.serializer()
            "chat_administrators" -> BotCommandScopeChatAdministrators.serializer()
            "chat_member" -> BotCommandScopeChatMember.serializer()
            else -> error("Unknown BotCommandScope type: $type")
        }
}

public object ChatMemberSerializer : JsonContentPolymorphicSerializer<ChatMember>(ChatMember::class) {
    override fun selectDeserializer(element: JsonElement): KSerializer<out ChatMember> =
        when (val status = element.jsonObject["status"]?.jsonPrimitive?.content) {
            "creator" -> ChatMemberOwner.serializer()
            "administrator" -> ChatMemberAdministrator.serializer()
            "member" -> ChatMemberMember.serializer()
            "restricted" -> ChatMemberRestricted.serializer()
            "left" -> ChatMemberLeft.serializer()
            "kicked" -> ChatMemberBanned.serializer()
            else -> error("Unknown ChatMember status: $status")
        }
}

public object InputMediaSerializer : JsonContentPolymorphicSerializer<InputMedia>(InputMedia::class) {
    override fun selectDeserializer(element: JsonElement): KSerializer<out InputMedia> =
        when (val type = element.jsonObject["type"]?.jsonPrimitive?.content) {
            "animation" -> InputMediaAnimation.serializer()
            "document" -> InputMediaDocument.serializer()
            "audio" -> InputMediaAudio.serializer()
            "photo" -> InputMediaPhoto.serializer()
            "video" -> InputMediaVideo.serializer()
            else -> error("Unknown InputMedia type: $type")
        }
}

public object InlineQueryResultSerializer : JsonContentPolymorphicSerializer<InlineQueryResult>(InlineQueryResult::class) {
    override fun selectDeserializer(
        element: JsonElement,
    ): KSerializer<out InlineQueryResult> =
        TODO("No need to deserialize InlineQueryResult entity")
}

public object InputMessageContentSerializer : JsonContentPolymorphicSerializer<InputMessageContent>(InputMessageContent::class) {
    override fun selectDeserializer(
        element: JsonElement,
    ): KSerializer<out InputMessageContent> =
        TODO("No need to deserialize InputMessageContent entity")
}

public object MenuButtonSerializer : JsonContentPolymorphicSerializer<MenuButton>(MenuButton::class) {
    override fun selectDeserializer(
        element: JsonElement,
    ): KSerializer<out MenuButton> =
        TODO("No need to deserialize MenuButton entity")
}

public object PassportElementErrorSerializer : JsonContentPolymorphicSerializer<PassportElementError>(PassportElementError::class) {
    override fun selectDeserializer(element: JsonElement): KSerializer<out PassportElementError> =
        when (val type = element.jsonObject["type"]?.jsonPrimitive?.content) {
            else -> error("Unknown argument type: $type")
        }
}

public object ChatIdSerializer : JsonContentPolymorphicSerializer<ChatId>(ChatId::class) {
    override fun selectDeserializer(element: JsonElement): KSerializer<out ChatId> =
        element.jsonPrimitive.let { jsonPrimitive ->
            when {
                jsonPrimitive.isString -> StringChatId.serializer()
                jsonPrimitive.content.toLongOrNull() != null -> LongChatId.serializer()
                else -> error("Unknown argument type: ${jsonPrimitive.content}")
            }
        }
}

public object ThumbnailSerializer : JsonContentPolymorphicSerializer<Thumbnail>(Thumbnail::class) {
    override fun selectDeserializer(element: JsonElement): KSerializer<out Thumbnail> =
        when (val type = element.jsonObject["type"]?.jsonPrimitive?.content) {
            else -> error("Unknown argument type: $type")
        }
}

public object MessageOrTrueSerializer : JsonContentPolymorphicSerializer<MessageOrTrue>(MessageOrTrue::class) {
    override fun selectDeserializer(element: JsonElement): KSerializer<out MessageOrTrue> =
        when (element) {
            is JsonPrimitive -> BooleanValue.serializer()
            else -> MessageValue.serializer()
        }
}

public object ReactionTypeSerializer : JsonContentPolymorphicSerializer<ReactionType>(ReactionType::class) {
    override fun selectDeserializer(element: JsonElement): KSerializer<out ReactionType> =
        when (val type = element.jsonObject["type"]?.jsonPrimitive?.content) {
            "emoji" -> ReactionTypeEmoji.serializer()
            "custom_emoji" -> ReactionTypeCustomEmoji.serializer()
            else -> error("Unknown argument type: $type")
        }
}

public class MaybeInaccessibleMessageSerializer : JsonContentPolymorphicSerializer<MaybeInaccessibleMessage>(MaybeInaccessibleMessage::class) {
    override fun selectDeserializer(element: JsonElement): KSerializer<out MaybeInaccessibleMessage> =
        when (element.jsonObject["date"]?.jsonPrimitive?.content) {
            "0" -> InaccessibleMessage.serializer()
            else -> Message.serializer()
        }
}

public class ChatBoostSourceSerializer : JsonContentPolymorphicSerializer<ChatBoostSource>(ChatBoostSource::class) {
    override fun selectDeserializer(element: JsonElement): KSerializer<out ChatBoostSource> =
        when (val type = element.jsonObject["source"]?.jsonPrimitive?.content) {

            "premium" -> ChatBoostSourcePremium.serializer()
            "gift_code" -> ChatBoostSourceGiftCode.serializer()
            "giveaway" -> ChatBoostSourceGiveaway.serializer()
            else -> error("Unknown argument type: $type")
        }
}

public class MessageOriginSerializer : JsonContentPolymorphicSerializer<MessageOrigin>(MessageOrigin::class) {
    override fun selectDeserializer(element: JsonElement): KSerializer<out MessageOrigin> =
        when (val type = element.jsonObject["type"]?.jsonPrimitive?.content) {
            "user" -> MessageOriginUser.serializer()
            "hidden_user" -> MessageOriginHiddenUser.serializer()
            "chat" -> MessageOriginChat.serializer()
            "channel" -> MessageOriginChannel.serializer()
            else -> error("Unknown argument type: $type")
        }
}
