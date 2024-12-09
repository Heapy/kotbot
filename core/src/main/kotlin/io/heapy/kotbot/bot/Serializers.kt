package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.model.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.*

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

public object InlineQueryResultSerializer :
    JsonContentPolymorphicSerializer<InlineQueryResult>(InlineQueryResult::class) {
    override fun selectDeserializer(
        element: JsonElement,
    ): KSerializer<out InlineQueryResult> =
        error("No need to deserialize InlineQueryResult entity")
}

public object InputMessageContentSerializer :
    JsonContentPolymorphicSerializer<InputMessageContent>(InputMessageContent::class) {
    override fun selectDeserializer(
        element: JsonElement,
    ): KSerializer<out InputMessageContent> =
        error("No need to deserialize InputMessageContent entity")
}

public object MenuButtonSerializer : JsonContentPolymorphicSerializer<MenuButton>(MenuButton::class) {
    override fun selectDeserializer(
        element: JsonElement,
    ): KSerializer<out MenuButton> =
        error("No need to deserialize MenuButton entity")
}

public object PassportElementErrorSerializer :
    JsonContentPolymorphicSerializer<PassportElementError>(PassportElementError::class) {
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
            "paid" -> ReactionTypePaid.serializer()
            else -> error("Unknown argument type: $type")
        }
}

public class MaybeInaccessibleMessageSerializer :
    JsonContentPolymorphicSerializer<MaybeInaccessibleMessage>(MaybeInaccessibleMessage::class) {
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

public class BackgroundFillSerializer : JsonContentPolymorphicSerializer<BackgroundFill>(BackgroundFill::class) {
    override fun selectDeserializer(element: JsonElement): KSerializer<out BackgroundFill> =
        when (val type = element.jsonObject["type"]?.jsonPrimitive?.content) {
            "solid" -> BackgroundFillSolid.serializer()
            "gradient" -> BackgroundFillGradient.serializer()
            "freeform_gradient" -> BackgroundFillFreeformGradient.serializer()
            else -> error("Unknown argument type: $type")
        }
}

public class BackgroundTypeSerializer : JsonContentPolymorphicSerializer<BackgroundType>(BackgroundType::class) {
    override fun selectDeserializer(element: JsonElement): KSerializer<out BackgroundType> =
        when (val type = element.jsonObject["type"]?.jsonPrimitive?.content) {
            "chat_theme" -> BackgroundTypeChatTheme.serializer()
            "fill" -> BackgroundTypeFill.serializer()
            "pattern" -> BackgroundTypePattern.serializer()
            "wallpaper" -> BackgroundTypeWallpaper.serializer()
            else -> error("Unknown argument type: $type")
        }
}

public class InputPaidMediaSerializer : JsonContentPolymorphicSerializer<InputPaidMedia>(InputPaidMedia::class) {
    override fun selectDeserializer(element: JsonElement): KSerializer<out InputPaidMedia> =
        when (val type = element.jsonObject["type"]?.jsonPrimitive?.content) {
            "photo" -> InputPaidMediaPhoto.serializer()
            "video" -> InputPaidMediaVideo.serializer()
            else -> error("Unknown argument type: $type")
        }
}

public class PaidMediaSerializer : JsonContentPolymorphicSerializer<PaidMedia>(PaidMedia::class) {
    override fun selectDeserializer(element: JsonElement): KSerializer<out PaidMedia> =
        when (val type = element.jsonObject["type"]?.jsonPrimitive?.content) {
            "preview" -> PaidMediaPreview.serializer()
            "photo" -> PaidMediaPhoto.serializer()
            "video" -> PaidMediaVideo.serializer()
            else -> error("Unknown argument type: $type")
        }
}

public class RevenueWithdrawalStateSerializer : JsonContentPolymorphicSerializer<RevenueWithdrawalState>(RevenueWithdrawalState::class) {
    override fun selectDeserializer(element: JsonElement): KSerializer<out RevenueWithdrawalState> =
        when (val type = element.jsonObject["type"]?.jsonPrimitive?.content) {
            "failed" -> RevenueWithdrawalStateFailed.serializer()
            "pending" -> RevenueWithdrawalStatePending.serializer()
            "succeeded" -> RevenueWithdrawalStateSucceeded.serializer()
            else -> error("Unknown argument type: $type")
        }
}

public class TransactionPartnerSerializer : JsonContentPolymorphicSerializer<TransactionPartner>(TransactionPartner::class) {
    override fun selectDeserializer(element: JsonElement): KSerializer<out TransactionPartner> =
        when (val type = element.jsonObject["type"]?.jsonPrimitive?.content) {
            "user" -> TransactionPartnerUser.serializer()
            "affiliate_program" -> TransactionPartnerAffiliateProgram.serializer()
            "fragment" -> TransactionPartnerFragment.serializer()
            "telegram_ads" -> TransactionPartnerTelegramAds.serializer()
            "telegram_api" -> TransactionPartnerTelegramApi.serializer()
            "other" -> TransactionPartnerOther.serializer()
            else -> error("Unknown argument type: $type")
        }
}
