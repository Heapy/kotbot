package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.BotCommand
import io.heapy.kotbot.bot.model.BotCommandScope
import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to change the list of the bot's commands. See [this manual](https://core.telegram.org/bots/features#commands) for more details about bot commands. Returns *True* on success.
 */
@Serializable
public data class SetMyCommands(
    /**
     * A JSON-serialized list of bot commands to be set as the list of the bot's commands. At most 100 commands can be specified.
     */
    public val commands: List<BotCommand>,
    /**
     * A JSON-serialized object, describing scope of users for which the commands are relevant. Defaults to [BotCommandScopeDefault](https://core.telegram.org/bots/api/#botcommandscopedefault).
     */
    public val scope: BotCommandScope? = null,
    /**
     * A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from the given scope, for whose language there are no dedicated commands
     */
    public val language_code: String? = null,
) : Method<SetMyCommands, Boolean> by Companion {
    public companion object : Method<SetMyCommands, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<SetMyCommands> = serializer()

        override val _name: String = "setMyCommands"
    }
}
