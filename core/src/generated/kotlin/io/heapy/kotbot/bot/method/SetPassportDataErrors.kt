package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.PassportElementError
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Informs a user that some of the Telegram Passport elements they provided contains errors. The user will not be able to re-submit their Passport to you until the errors are fixed (the contents of the field for which you returned the error must change). Returns *True* on success.
 *
 * Use this if the data submitted by the user doesn't satisfy the standards your service requires for any reason. For example, if a birthday date seems invalid, a submitted document is blurry, a scan shows evidence of tampering, etc. Supply some details in the error message to make sure the user knows how to correct the issues.
 */
@Serializable
public data class SetPassportDataErrors(
    /**
     * User identifier
     */
    public val user_id: Long,
    /**
     * A JSON-serialized array describing the errors
     */
    public val errors: List<PassportElementError>,
) : Method<SetPassportDataErrors, Boolean> by Companion {
    public companion object : Method<SetPassportDataErrors, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<SetPassportDataErrors> = serializer()

        override val _name: String = "setPassportDataErrors"
    }
}
