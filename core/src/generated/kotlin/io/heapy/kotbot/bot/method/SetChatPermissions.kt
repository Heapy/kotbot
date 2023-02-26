package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.ChatPermissions
import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to set default chat permissions for all members. The bot must be an administrator in the group or a supergroup for this to work and must have the *can_restrict_members* administrator rights. Returns *True* on success.
 */
@Serializable
public data class SetChatPermissions(
    /**
     * Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
     */
    public val chat_id: ChatId,
    /**
     * A JSON-serialized object for new default chat permissions
     */
    public val permissions: ChatPermissions,
    /**
     * Pass *True* if chat permissions are set independently. Otherwise, the *can_send_other_messages* and *can_add_web_page_previews* permissions will imply the *can_send_messages*, *can_send_audios*, *can_send_documents*, *can_send_photos*, *can_send_videos*, *can_send_video_notes*, and *can_send_voice_notes* permissions; the *can_send_polls* permission will imply the *can_send_messages* permission.
     */
    public val use_independent_chat_permissions: Boolean? = null,
) : Method<SetChatPermissions, Boolean> by Companion {
    public companion object : Method<SetChatPermissions, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<SetChatPermissions> = serializer()

        override val _name: String = "setChatPermissions"
    }
}
