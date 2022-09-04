package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.AnyOfObject
import io.heapy.kotbot.bot.InlineQueryResultSerializer
import kotlinx.serialization.Serializable

/**
 * This object represents one result of an inline query. Telegram clients currently support results of the following 20 types:
 *
 * * [InlineQueryResultCachedAudio](https://core.telegram.org/bots/api/#inlinequeryresultcachedaudio)
 * * [InlineQueryResultCachedDocument](https://core.telegram.org/bots/api/#inlinequeryresultcacheddocument)
 * * [InlineQueryResultCachedGif](https://core.telegram.org/bots/api/#inlinequeryresultcachedgif)
 * * [InlineQueryResultCachedMpeg4Gif](https://core.telegram.org/bots/api/#inlinequeryresultcachedmpeg4gif)
 * * [InlineQueryResultCachedPhoto](https://core.telegram.org/bots/api/#inlinequeryresultcachedphoto)
 * * [InlineQueryResultCachedSticker](https://core.telegram.org/bots/api/#inlinequeryresultcachedsticker)
 * * [InlineQueryResultCachedVideo](https://core.telegram.org/bots/api/#inlinequeryresultcachedvideo)
 * * [InlineQueryResultCachedVoice](https://core.telegram.org/bots/api/#inlinequeryresultcachedvoice)
 * * [InlineQueryResultArticle](https://core.telegram.org/bots/api/#inlinequeryresultarticle)
 * * [InlineQueryResultAudio](https://core.telegram.org/bots/api/#inlinequeryresultaudio)
 * * [InlineQueryResultContact](https://core.telegram.org/bots/api/#inlinequeryresultcontact)
 * * [InlineQueryResultGame](https://core.telegram.org/bots/api/#inlinequeryresultgame)
 * * [InlineQueryResultDocument](https://core.telegram.org/bots/api/#inlinequeryresultdocument)
 * * [InlineQueryResultGif](https://core.telegram.org/bots/api/#inlinequeryresultgif)
 * * [InlineQueryResultLocation](https://core.telegram.org/bots/api/#inlinequeryresultlocation)
 * * [InlineQueryResultMpeg4Gif](https://core.telegram.org/bots/api/#inlinequeryresultmpeg4gif)
 * * [InlineQueryResultPhoto](https://core.telegram.org/bots/api/#inlinequeryresultphoto)
 * * [InlineQueryResultVenue](https://core.telegram.org/bots/api/#inlinequeryresultvenue)
 * * [InlineQueryResultVideo](https://core.telegram.org/bots/api/#inlinequeryresultvideo)
 * * [InlineQueryResultVoice](https://core.telegram.org/bots/api/#inlinequeryresultvoice)
 */
@Serializable(with = InlineQueryResultSerializer::class)
@AnyOfObject
public sealed interface InlineQueryResult
