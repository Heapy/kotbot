package io.heapy.kotbot.bot.method

import kotlinx.serialization.Serializable

/**
 * A simple method for testing your bot's authentication token. Requires no parameters. Returns basic information about the bot in form of a [User](https://core.telegram.org/bots/api/#user) object.
 */
@Serializable
public class GetMe
