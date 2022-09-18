package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.ChatMemberSerializer
import kotlinx.serialization.Serializable

/**
 * This object contains information about one member of a chat. Currently, the following 6 types of chat members are supported:
 *
 * * [ChatMemberOwner](https://core.telegram.org/bots/api/#chatmemberowner)
 * * [ChatMemberAdministrator](https://core.telegram.org/bots/api/#chatmemberadministrator)
 * * [ChatMemberMember](https://core.telegram.org/bots/api/#chatmembermember)
 * * [ChatMemberRestricted](https://core.telegram.org/bots/api/#chatmemberrestricted)
 * * [ChatMemberLeft](https://core.telegram.org/bots/api/#chatmemberleft)
 * * [ChatMemberBanned](https://core.telegram.org/bots/api/#chatmemberbanned)
 */
@Serializable(with = ChatMemberSerializer::class)
public sealed interface ChatMember
