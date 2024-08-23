package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.RevenueWithdrawalStateSerializer
import kotlinx.serialization.Serializable

/**
 * This object describes the state of a revenue withdrawal operation. Currently, it can be one of
 *
 * * [RevenueWithdrawalStatePending](https://core.telegram.org/bots/api/#revenuewithdrawalstatepending)
 * * [RevenueWithdrawalStateSucceeded](https://core.telegram.org/bots/api/#revenuewithdrawalstatesucceeded)
 * * [RevenueWithdrawalStateFailed](https://core.telegram.org/bots/api/#revenuewithdrawalstatefailed)
 */
@Serializable(with = RevenueWithdrawalStateSerializer::class)
public sealed interface RevenueWithdrawalState
