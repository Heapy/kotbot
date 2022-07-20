package io.heapy.kotbot.bot

import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to unban a previously banned user in a supergroup or channel.
 * The user will not return to the group or channel automatically, but will be
 * able to join via link, etc. The bot must be an administrator for this to work.
 * By default, this method guarantees that after the call the user is not
 * a member of the chat, but will be able to join it. So if the user is a member
 * of the chat they will also be removed from the chat. If you don't want this,
 * use the parameter `only_if_banned`.
 * Returns True on success.
 */
@Serializable
public data class UnbanChatMember(
    /**
     * Unique identifier for the target group or username
     * of the target supergroup or channel (in the format @channelusername)
     */
    private val chat_id: String,
    /**
     * Unique identifier of the target user
     */
    private val user_id: Long,
    /**
     * Do nothing if the user is not banned
     */
    private val only_if_banned: Boolean? = null,
) : Method<Boolean> {
    @Transient
    private val deserializer: KSerializer<Response<Boolean>> =
        Response.serializer(Boolean.serializer())

    override suspend fun Kotbot.execute(): Boolean {
        return requestForJson(
            name = "unbanChatMember",
            serialize = {
                json.encodeToString(
                    serializer(),
                    this@UnbanChatMember
                )
            },
            deserialize = {
                json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
            }
        )
    }
}
