package io.heapy.kotbot.experimental

import io.heapy.kotbot.ApplicationFactory
import io.heapy.kotbot.Command
import io.heapy.kotbot.Filter
import io.heapy.kotbot.Rule
import io.heapy.kotbot.bot.Update
import kotlin.properties.ReadOnlyProperty

val `@user_chat` by userChat()
val `@kotlin_lang` by groupChat(-1001032833563)
val `@mipt_npm` by groupChat(-1001185393451)

fun groupChat(id: Long) : ReadOnlyProperty<Any?, EmptyChatConfiguration> {
    return ReadOnlyProperty { _, property ->
        EmptyChatConfiguration(
            GroupChat(
                id = id,
                name = property.name,
            )
        )
    }
}

fun userChat() : ReadOnlyProperty<Any?, EmptyChatConfiguration> {
    return ReadOnlyProperty { _, _ ->
        EmptyChatConfiguration(
            UserChat
        )
    }
}

interface BotConfiguration {
    val configs: List<ChatConfiguration>
}

class BotConfigurationBuilder : BotConfiguration {
    override val configs: MutableList<ChatConfiguration> = mutableListOf()
}

interface ChatConfiguration {
    val chat: Chat
    val rules: List<Rule>
    val filters: List<Filter>
    val commands: List<Command>
}

sealed interface Chat

data class GroupChat(
    val id: Long,
    val name: String,
) : Chat

data class ChannelChat(
    val id: Long,
    val name: String,
) : Chat

object UserChat : Chat

class ChatConfigurationBuilder : ChatConfiguration {
    override var chat: Chat = UserChat
    override var rules: List<Rule> = emptyList()
    override var filters: List<Filter> = emptyList()
    override var commands: List<Command> = emptyList()
}

class RulesBuilder {
    val rules: MutableList<Rule> = mutableListOf()
}

fun ChatConfigurationBuilder.rules(
    builderAction: RulesBuilder.() -> Unit,
) {
    rules = RulesBuilder().apply(builderAction).rules
}

data class EmptyChatConfiguration(
    override val chat: Chat,
) : ChatConfiguration {
    override val rules: List<Rule> = emptyList()
    override val filters: List<Filter> = emptyList()
    override val commands: List<Command> = emptyList()
}

fun bot(
    builderAction: BotConfigurationBuilder.() -> Unit,
): BotConfiguration {
    return BotConfigurationBuilder().apply(builderAction)
}

context(BotConfigurationBuilder)
operator fun EmptyChatConfiguration.invoke(
    builderAction: ChatConfigurationBuilder.() -> Unit,
) {
    configs.add(
        ChatConfigurationBuilder()
            .also { it.chat = chat }
            .apply(builderAction)
    )
}

context(ApplicationFactory)
fun RulesBuilder.deleteJoin() {
    rules.add(deleteJoinRule)
}

context(ApplicationFactory)
fun RulesBuilder.deleteHello() {
    rules.add(deleteHelloRule)
}

context(ApplicationFactory)
fun RulesBuilder.longTimeNoSee() {
    rules.add(longTimeNoSeeRule)
}

context(ApplicationFactory)
fun RulesBuilder.deleteSwearing() {
    rules.add(deleteSwearingRule)
}

context(ApplicationFactory)
fun RulesBuilder.deleteSpam() {
    rules.add(deleteSpamRule)
}

context(ApplicationFactory)
fun RulesBuilder.deleteVoiceMessage() {
    rules.add(deleteVoiceMessageRule)
}

context(ApplicationFactory)
fun RulesBuilder.deleteSticker() {
    rules.add(deleteStickersRule)
}

context(ApplicationFactory)
fun RulesBuilder.combotCasRule() {
    rules.add(combotCasRule)
}

context(ApplicationFactory)
fun RulesBuilder.defaultSet() {
    deleteJoin()
    deleteHello()
    longTimeNoSee()
    deleteSwearing()
    deleteSpam()
    deleteVoiceMessage()
    combotCasRule()
}

fun BotConfiguration.route(update: Update): ChatConfiguration? {
    return configs.singleOrNull { config ->
        when(val currentChat = config.chat) {
            is UserChat -> TODO()
            is GroupChat -> TODO()
            is ChannelChat -> TODO()
        }
    }
}

context(ApplicationFactory)
fun botConfiguration(): BotConfiguration {
    return bot {
        `@user_chat` {
        }
        `@kotlin_lang` {
            rules {
                defaultSet()
                deleteSticker()
            }
        }
        `@mipt_npm` {
            rules {
                defaultSet()
            }
        }
    }
}
