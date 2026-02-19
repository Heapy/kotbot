package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.model.BackgroundFill
import io.heapy.kotbot.bot.model.BackgroundFillFreeformGradient
import io.heapy.kotbot.bot.model.BackgroundFillGradient
import io.heapy.kotbot.bot.model.BackgroundFillSolid
import io.heapy.kotbot.bot.model.BackgroundType
import io.heapy.kotbot.bot.model.BackgroundTypeChatTheme
import io.heapy.kotbot.bot.model.BackgroundTypeFill
import io.heapy.kotbot.bot.model.BackgroundTypePattern
import io.heapy.kotbot.bot.model.BackgroundTypeWallpaper
import io.heapy.kotbot.bot.model.BooleanValue
import io.heapy.kotbot.bot.model.BotCommandScope
import io.heapy.kotbot.bot.model.BotCommandScopeAllChatAdministrators
import io.heapy.kotbot.bot.model.BotCommandScopeAllGroupChats
import io.heapy.kotbot.bot.model.BotCommandScopeAllPrivateChats
import io.heapy.kotbot.bot.model.BotCommandScopeChat
import io.heapy.kotbot.bot.model.BotCommandScopeChatAdministrators
import io.heapy.kotbot.bot.model.BotCommandScopeChatMember
import io.heapy.kotbot.bot.model.BotCommandScopeDefault
import io.heapy.kotbot.bot.model.ChatBoostSource
import io.heapy.kotbot.bot.model.ChatBoostSourceGiftCode
import io.heapy.kotbot.bot.model.ChatBoostSourceGiveaway
import io.heapy.kotbot.bot.model.ChatBoostSourcePremium
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.ChatMember
import io.heapy.kotbot.bot.model.ChatMemberAdministrator
import io.heapy.kotbot.bot.model.ChatMemberBanned
import io.heapy.kotbot.bot.model.ChatMemberLeft
import io.heapy.kotbot.bot.model.ChatMemberMember
import io.heapy.kotbot.bot.model.ChatMemberOwner
import io.heapy.kotbot.bot.model.ChatMemberRestricted
import io.heapy.kotbot.bot.model.InaccessibleMessage
import io.heapy.kotbot.bot.model.InlineQueryResult
import io.heapy.kotbot.bot.model.InputMedia
import io.heapy.kotbot.bot.model.InputMediaAnimation
import io.heapy.kotbot.bot.model.InputMediaAudio
import io.heapy.kotbot.bot.model.InputMediaDocument
import io.heapy.kotbot.bot.model.InputMediaPhoto
import io.heapy.kotbot.bot.model.InputMediaVideo
import io.heapy.kotbot.bot.model.InputMessageContent
import io.heapy.kotbot.bot.model.InputPaidMedia
import io.heapy.kotbot.bot.model.InputPaidMediaPhoto
import io.heapy.kotbot.bot.model.InputPaidMediaVideo
import io.heapy.kotbot.bot.model.InputProfilePhoto
import io.heapy.kotbot.bot.model.InputProfilePhotoAnimated
import io.heapy.kotbot.bot.model.InputProfilePhotoStatic
import io.heapy.kotbot.bot.model.InputStoryContent
import io.heapy.kotbot.bot.model.InputStoryContentPhoto
import io.heapy.kotbot.bot.model.InputStoryContentVideo
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.model.MaybeInaccessibleMessage
import io.heapy.kotbot.bot.model.MenuButton
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.MessageOrTrue
import io.heapy.kotbot.bot.model.MessageOrigin
import io.heapy.kotbot.bot.model.MessageOriginChannel
import io.heapy.kotbot.bot.model.MessageOriginChat
import io.heapy.kotbot.bot.model.MessageOriginHiddenUser
import io.heapy.kotbot.bot.model.MessageOriginUser
import io.heapy.kotbot.bot.model.MessageValue
import io.heapy.kotbot.bot.model.OwnedGift
import io.heapy.kotbot.bot.model.OwnedGiftRegular
import io.heapy.kotbot.bot.model.OwnedGiftUnique
import io.heapy.kotbot.bot.model.PaidMedia
import io.heapy.kotbot.bot.model.PaidMediaPhoto
import io.heapy.kotbot.bot.model.PaidMediaPreview
import io.heapy.kotbot.bot.model.PaidMediaVideo
import io.heapy.kotbot.bot.model.PassportElementError
import io.heapy.kotbot.bot.model.ReactionType
import io.heapy.kotbot.bot.model.ReactionTypeCustomEmoji
import io.heapy.kotbot.bot.model.ReactionTypeEmoji
import io.heapy.kotbot.bot.model.ReactionTypePaid
import io.heapy.kotbot.bot.model.RevenueWithdrawalState
import io.heapy.kotbot.bot.model.RevenueWithdrawalStateFailed
import io.heapy.kotbot.bot.model.RevenueWithdrawalStatePending
import io.heapy.kotbot.bot.model.RevenueWithdrawalStateSucceeded
import io.heapy.kotbot.bot.model.StoryAreaType
import io.heapy.kotbot.bot.model.StoryAreaTypeLink
import io.heapy.kotbot.bot.model.StoryAreaTypeLocation
import io.heapy.kotbot.bot.model.StoryAreaTypeSuggestedReaction
import io.heapy.kotbot.bot.model.StoryAreaTypeUniqueGift
import io.heapy.kotbot.bot.model.StoryAreaTypeWeather
import io.heapy.kotbot.bot.model.StringChatId
import io.heapy.kotbot.bot.model.Thumbnail
import io.heapy.kotbot.bot.model.TransactionPartner
import io.heapy.kotbot.bot.model.TransactionPartnerAffiliateProgram
import io.heapy.kotbot.bot.model.TransactionPartnerChat
import io.heapy.kotbot.bot.model.TransactionPartnerFragment
import io.heapy.kotbot.bot.model.TransactionPartnerOther
import io.heapy.kotbot.bot.model.TransactionPartnerTelegramAds
import io.heapy.kotbot.bot.model.TransactionPartnerTelegramApi
import io.heapy.kotbot.bot.model.TransactionPartnerUser
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
            "chat" -> TransactionPartnerChat.serializer()
            "other" -> TransactionPartnerOther.serializer()
            else -> error("Unknown argument type: $type")
        }
}

public class StoryAreaTypeSerializer : JsonContentPolymorphicSerializer<StoryAreaType>(StoryAreaType::class) {
    override fun selectDeserializer(element: JsonElement): KSerializer<out StoryAreaType> =
        when (val type = element.jsonObject["type"]?.jsonPrimitive?.content) {
            "location" -> StoryAreaTypeLocation.serializer()
            "suggested_reaction" -> StoryAreaTypeSuggestedReaction.serializer()
            "link" -> StoryAreaTypeLink.serializer()
            "weather" -> StoryAreaTypeWeather.serializer()
            "unique_gift" -> StoryAreaTypeUniqueGift.serializer()
            else -> error("Unknown argument type: $type")
        }
}

public class InputStoryContentSerializer :
    JsonContentPolymorphicSerializer<InputStoryContent>(InputStoryContent::class) {
    override fun selectDeserializer(element: JsonElement): KSerializer<out InputStoryContent> =
        when (val type = element.jsonObject["type"]?.jsonPrimitive?.content) {
            "photo" -> InputStoryContentPhoto.serializer()
            "video" -> InputStoryContentVideo.serializer()
            else -> error("Unknown argument type: $type")
        }
}

public class InputProfilePhotoSerializer :
    JsonContentPolymorphicSerializer<InputProfilePhoto>(InputProfilePhoto::class) {
    override fun selectDeserializer(element: JsonElement): KSerializer<out InputProfilePhoto> =
        when (val type = element.jsonObject["type"]?.jsonPrimitive?.content) {
            "static" -> InputProfilePhotoStatic.serializer()
            "animated" -> InputProfilePhotoAnimated.serializer()
            else -> error("Unknown argument type: $type")
        }
}

public class OwnedGiftSerializer : JsonContentPolymorphicSerializer<OwnedGift>(OwnedGift::class) {
    override fun selectDeserializer(element: JsonElement): KSerializer<out OwnedGift> =
        when (val type = element.jsonObject["type"]?.jsonPrimitive?.content) {
            "regular" -> OwnedGiftRegular.serializer()
            "unique" -> OwnedGiftUnique.serializer()
            else -> error("Unknown argument type: $type")
        }
}
