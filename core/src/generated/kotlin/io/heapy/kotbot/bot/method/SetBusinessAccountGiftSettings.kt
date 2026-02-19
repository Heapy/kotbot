package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.AcceptedGiftTypes
import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Changes the privacy settings pertaining to incoming gifts in a managed business account. Requires the *can_change_gift_settings* business bot right. Returns *True* on success.
 */
@Serializable
public data class SetBusinessAccountGiftSettings(
    /**
     * Unique identifier of the business connection
     */
    public val business_connection_id: String,
    /**
     * Pass *True*, if a button for sending a gift to the user or by the business account must always be shown in the input field
     */
    public val show_gift_button: Boolean,
    /**
     * Types of gifts accepted by the business account
     */
    public val accepted_gift_types: AcceptedGiftTypes,
) : Method<SetBusinessAccountGiftSettings, Boolean> by Companion {
    public companion object : Method<SetBusinessAccountGiftSettings, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<SetBusinessAccountGiftSettings> = serializer()

        override val _name: String = "setBusinessAccountGiftSettings"
    }
}
