package io.heapy.kotbot.bot.method

import kotlin.Boolean
import kotlinx.serialization.Serializable

/**
 * Use this method to get the current default administrator rights of the bot. Returns [ChatAdministratorRights](https://core.telegram.org/bots/api/#chatadministratorrights) on success.
 */
@Serializable
public data class GetMyDefaultAdministratorRights(
  /**
   * Pass *True* to get default administrator rights of the bot in channels. Otherwise, default administrator rights of the bot for groups and supergroups will be returned.
   */
  public val for_channels: Boolean? = null,
)
