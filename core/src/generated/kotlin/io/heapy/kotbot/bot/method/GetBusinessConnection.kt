package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.BusinessConnection
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to get information about the connection of the bot with a business account. Returns a [BusinessConnection](https://core.telegram.org/bots/api/#businessconnection) object on success.
 */
@Serializable
public data class GetBusinessConnection(
    /**
     * Unique identifier of the business connection
     */
    public val business_connection_id: String,
) : Method<GetBusinessConnection, BusinessConnection> by Companion {
    public companion object : Method<GetBusinessConnection, BusinessConnection> {
        override val _deserializer: KSerializer<Response<BusinessConnection>> =
                Response.serializer(BusinessConnection.serializer())

        override val _serializer: KSerializer<GetBusinessConnection> = serializer()

        override val _name: String = "getBusinessConnection"
    }
}
