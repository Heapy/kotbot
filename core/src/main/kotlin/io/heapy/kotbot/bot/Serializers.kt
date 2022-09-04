package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.model.BotCommandScope
import io.heapy.kotbot.bot.model.BotCommandScopeAllPrivateChats
import io.heapy.kotbot.bot.model.BotCommandScopeDefault
import io.heapy.kotbot.bot.model.ChatMember
import io.heapy.kotbot.bot.model.InlineQueryResult
import io.heapy.kotbot.bot.model.InputMedia
import io.heapy.kotbot.bot.model.InputMessageContent
import io.heapy.kotbot.bot.model.MenuButton
import io.heapy.kotbot.bot.model.PassportElementError
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

public object BotCommandScopeSerializer : JsonContentPolymorphicSerializer<BotCommandScope>(BotCommandScope::class) {
    override fun selectDeserializer(element: JsonElement): KSerializer<out BotCommandScope> =
        when (val type = element.jsonObject["type"]?.jsonPrimitive?.content) {
//            "default" -> BotCommandScopeDefault.serializer()
//            "all_private_chats" -> BotCommandScopeAllPrivateChats.serializer()
            else -> error("Unknown argument type: $type")
        }
}

public object ChatMemberSerializer : JsonContentPolymorphicSerializer<ChatMember>(ChatMember::class) {
    override fun selectDeserializer(element: JsonElement): KSerializer<out ChatMember> =
        when (val type = element.jsonObject["type"]?.jsonPrimitive?.content) {
            else -> error("Unknown argument type: $type")
        }
}

public object InlineQueryResultSerializer : JsonContentPolymorphicSerializer<InlineQueryResult>(InlineQueryResult::class) {
    override fun selectDeserializer(element: JsonElement): KSerializer<out InlineQueryResult> =
        when (val type = element.jsonObject["type"]?.jsonPrimitive?.content) {
            else -> error("Unknown argument type: $type")
        }
}

public object InputMediaSerializer : JsonContentPolymorphicSerializer<InputMedia>(InputMedia::class) {
    override fun selectDeserializer(element: JsonElement): KSerializer<out InputMedia> =
        when (val type = element.jsonObject["type"]?.jsonPrimitive?.content) {
            else -> error("Unknown argument type: $type")
        }
}

public object InputMessageContentSerializer : JsonContentPolymorphicSerializer<InputMessageContent>(InputMessageContent::class) {
    override fun selectDeserializer(element: JsonElement): KSerializer<out InputMessageContent> =
        when (val type = element.jsonObject["type"]?.jsonPrimitive?.content) {
            else -> error("Unknown argument type: $type")
        }
}

public object MenuButtonSerializer : JsonContentPolymorphicSerializer<MenuButton>(MenuButton::class) {
    override fun selectDeserializer(element: JsonElement): KSerializer<out MenuButton> =
        when (val type = element.jsonObject["type"]?.jsonPrimitive?.content) {
            else -> error("Unknown argument type: $type")
        }
}

public object PassportElementErrorSerializer : JsonContentPolymorphicSerializer<PassportElementError>(PassportElementError::class) {
    override fun selectDeserializer(element: JsonElement): KSerializer<out PassportElementError> =
        when (val type = element.jsonObject["type"]?.jsonPrimitive?.content) {
            else -> error("Unknown argument type: $type")
        }
}
