package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to delete a group sticker set from a supergroup. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Use the field *can_set_sticker_set* optionally returned in [getChat](https://core.telegram.org/bots/api/#getchat) requests to check if the bot can use this method. Returns *True* on success.
 */
@Serializable
public data class DeleteChatStickerSet(
    /**
     * Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
     */
    public val chat_id: ChatId,
) : Method<DeleteChatStickerSet, Boolean> by Companion {
    public companion object : Method<DeleteChatStickerSet, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<DeleteChatStickerSet> = serializer()

        override val _name: String = "deleteChatStickerSet"
    }
}
