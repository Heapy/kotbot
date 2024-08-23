package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.TransactionPartnerSerializer
import kotlinx.serialization.Serializable

/**
 * This object describes the source of a transaction, or its recipient for outgoing transactions. Currently, it can be one of
 *
 * * [TransactionPartnerUser](https://core.telegram.org/bots/api/#transactionpartneruser)
 * * [TransactionPartnerFragment](https://core.telegram.org/bots/api/#transactionpartnerfragment)
 * * [TransactionPartnerTelegramAds](https://core.telegram.org/bots/api/#transactionpartnertelegramads)
 * * [TransactionPartnerOther](https://core.telegram.org/bots/api/#transactionpartnerother)
 */
@Serializable(with = TransactionPartnerSerializer::class)
public sealed interface TransactionPartner
