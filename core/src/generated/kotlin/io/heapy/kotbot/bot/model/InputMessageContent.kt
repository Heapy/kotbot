package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.InputMessageContentSerializer
import kotlinx.serialization.Serializable

/**
 * This object represents the content of a message to be sent as a result of an inline query. Telegram clients currently support the following 5 types:
 *
 * * [InputTextMessageContent](https://core.telegram.org/bots/api/#inputtextmessagecontent)
 * * [InputLocationMessageContent](https://core.telegram.org/bots/api/#inputlocationmessagecontent)
 * * [InputVenueMessageContent](https://core.telegram.org/bots/api/#inputvenuemessagecontent)
 * * [InputContactMessageContent](https://core.telegram.org/bots/api/#inputcontactmessagecontent)
 * * [InputInvoiceMessageContent](https://core.telegram.org/bots/api/#inputinvoicemessagecontent)
 */
@Serializable(with = InputMessageContentSerializer::class)
public sealed interface InputMessageContent
