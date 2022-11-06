package io.heapy.kotbot.bot.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * This object represents a game. Use BotFather to create and edit games, their short names will act as unique identifiers.
 */
@Serializable
public data class Game(
    /**
     * Title of the game
     */
    public val title: String,
    /**
     * Description of the game
     */
    public val description: String,
    /**
     * Photo that will be displayed in the game message in chats.
     */
    public val photo: List<PhotoSize>,
    /**
     * *Optional*. Brief description of the game or high scores included in the game message. Can be automatically edited to include current high scores for the game when the bot calls [setGameScore](https://core.telegram.org/bots/api/#setgamescore), or manually edited using [editMessageText](https://core.telegram.org/bots/api/#editmessagetext). 0-4096 characters.
     */
    public val text: String? = null,
    /**
     * *Optional*. Special entities that appear in *text*, such as usernames, URLs, bot commands, etc.
     */
    public val text_entities: List<MessageEntity>? = null,
    /**
     * *Optional*. Animation that will be displayed in the game message in chats. Upload via [BotFather](https://t.me/botfather)
     */
    public val animation: Animation? = null,
)
