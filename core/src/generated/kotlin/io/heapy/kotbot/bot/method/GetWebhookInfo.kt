package io.heapy.kotbot.bot.method

import kotlinx.serialization.Serializable

/**
 * Use this method to get current webhook status. Requires no parameters. On success, returns a [WebhookInfo](https://core.telegram.org/bots/api/#webhookinfo) object. If the bot is using [getUpdates](https://core.telegram.org/bots/api/#getupdates), will return an object with the *url* field empty.
 */
@Serializable
public class GetWebhookInfo
