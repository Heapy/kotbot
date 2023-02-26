package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.Sticker
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer

/**
 * Use this method to get custom emoji stickers, which can be used as a forum topic icon by any user. Requires no parameters. Returns an Array of [Sticker](https://core.telegram.org/bots/api/#sticker) objects.
 */
@Serializable
public class GetForumTopicIconStickers : Method<GetForumTopicIconStickers, List<Sticker>> by
        Companion {
    public companion object : Method<GetForumTopicIconStickers, List<Sticker>> {
        override val _deserializer: KSerializer<Response<List<Sticker>>> =
                Response.serializer(ListSerializer(Sticker.serializer()))

        override val _serializer: KSerializer<GetForumTopicIconStickers> = serializer()

        override val _name: String = "getForumTopicIconStickers"
    }
}
