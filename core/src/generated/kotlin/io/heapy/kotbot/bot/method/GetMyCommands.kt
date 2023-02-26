package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.BotCommand
import io.heapy.kotbot.bot.model.BotCommandScope
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer

/**
 * Use this method to get the current list of the bot's commands for the given scope and user language. Returns an Array of [BotCommand](https://core.telegram.org/bots/api/#botcommand) objects. If commands aren't set, an empty list is returned.
 */
@Serializable
public data class GetMyCommands(
    /**
     * A JSON-serialized object, describing scope of users. Defaults to [BotCommandScopeDefault](https://core.telegram.org/bots/api/#botcommandscopedefault).
     */
    public val scope: BotCommandScope? = null,
    /**
     * A two-letter ISO 639-1 language code or an empty string
     */
    public val language_code: String? = null,
) : Method<GetMyCommands, List<BotCommand>> by Companion {
    public companion object : Method<GetMyCommands, List<BotCommand>> {
        override val _deserializer: KSerializer<Response<List<BotCommand>>> =
                Response.serializer(ListSerializer(BotCommand.serializer()))

        override val _serializer: KSerializer<GetMyCommands> = serializer()

        override val _name: String = "getMyCommands"
    }
}
