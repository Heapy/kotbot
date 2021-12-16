package io.heapy.kotbot.bot

import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to ban a user in a group, a supergroup or a channel.
 * In the case of supergroups and channels, the user will not be able to
 * return to the chat on their own using invite links, etc., unless unbanned
 * first. The bot must be an administrator in the chat for this to work
 * and must have the appropriate administrator rights.
 * Returns True on success.
 */
@Serializable
public data class BanChatMember(
    /**
     * Unique identifier for the target group or username of the target
     * supergroup or channel (in the format @channelusername)
     */
    private val chat_id: String,
    /**
     * Unique identifier of the target user
     */
    private val user_id: Int,
    /**
     * Date when the user will be unbanned, unix time. If user is banned
     * for more than 366 days or less than 30 seconds from the current
     * time they are considered to be banned forever. Applied for
     * supergroups and channels only.
     */
    private val until_date: Int?,
    /**
     * Pass True to delete all messages from the chat for the user that
     * is being removed. If False, the user will be able to see messages
     * in the group that were sent before the user was removed. Always
     * True for supergroups and channels.
     */
    private val revoke_messages: Boolean?,
) : ApiMethod<Boolean> {
    @Transient
    private val deserializer: KSerializer<ApiResponse<Boolean>> =
        ApiResponse.serializer(Boolean.serializer())

    override suspend fun Kotbot.execute(): Boolean {
        return requestForJson(
            name = "banChatMember",
            serialize = {
                json.encodeToString(
                    serializer(),
                    this@BanChatMember
                )
            },
            deserialize = {
                json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
            }
        )
    }
}
