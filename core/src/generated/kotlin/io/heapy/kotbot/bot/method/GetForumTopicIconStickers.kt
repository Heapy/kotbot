package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.Sticker
import io.heapy.kotbot.bot.requestForJson
import io.heapy.kotbot.bot.unwrap
import io.ktor.client.statement.bodyAsText
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer

/**
 * Use this method to get custom emoji stickers, which can be used as a forum topic icon by any user. Requires no parameters. Returns an Array of [Sticker](https://core.telegram.org/bots/api/#sticker) objects.
 */
@Serializable
public class GetForumTopicIconStickers : Method<List<Sticker>> {
    public override suspend fun Kotbot.execute(): List<Sticker> = requestForJson(
        name = "getForumTopicIconStickers",
        serialize = {
            json.encodeToString(
                serializer(),
                this@GetForumTopicIconStickers
            )
        },
        deserialize = {
            json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
        },
    )

    public companion object {
        public val deserializer: KSerializer<Response<List<Sticker>>> =
                Response.serializer(ListSerializer(Sticker.serializer()))
    }
}
