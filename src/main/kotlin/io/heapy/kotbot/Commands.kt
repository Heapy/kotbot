package io.heapy.kotbot

import io.heapy.kotbot.Command.*
import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.SendMessage
import io.heapy.kotbot.bot.Update
import io.heapy.kotbot.bot.Update.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

interface Command {
    val info: Info

    fun execute(
        message: Message,
        update: Update,
        args: List<String>
    ): Flow<Method<*>>

    interface Info {
        val name: String
        val arity: Int
        val context: Context
        val access: Access
    }

    enum class Context {
        USER_CHAT,
        GROUP_CHAT
    }

    enum class Access {
        USER,
        ADMIN
    }
}

data class CommandInfo(
    override val name: String,
    override val arity: Int = 0,
    override val context: Context = Context.USER_CHAT,
    override val access: Access = Access.USER
) : Info

object NoopCommand : Command {
    override fun execute(message: Message, update: Update, args: List<String>): Flow<Method<*>> {
        return flowOf()
    }

    override val info: Info
        get() = INFO

    private val INFO = CommandInfo(
        name = "!noop",
        access = Access.ADMIN
    )
}

class HelloWorldCommand(
    override val info: Info = INFO
) : Command {
    override fun execute(message: Message, update: Update, args: List<String>): Flow<Method<*>> {
        return flowOf(
            SendMessage(
                chat_id = message.chat.id.toString(),
                text = "Hello, World!"
            )
        )
    }

    companion object {
        private val INFO = CommandInfo(
            name = "!hello",
            access = Access.USER
        )
    }
}

class ChatInfoCommand(
    override val info: Info = INFO
) : Command {
    override fun execute(message: Message, update: Update, args: List<String>): Flow<Method<*>> {
        return flowOf(
            SendMessage(
                chat_id = message.chat.id.toString(),
                text = """
                Chat id: ${message.chat.id}
            """.trimIndent()
            )
        )
    }

    companion object {
        private val INFO = CommandInfo(
            name = "!chat-info",
            context = Context.GROUP_CHAT,
            access = Access.ADMIN
        )
    }
}

class ContactInfoCommand(
    override val info: Info = INFO
) : Command {
    override fun execute(message: Message, update: Update, args: List<String>): Flow<Method<*>> {
        val contact = message.reply_to_message?.contact
        return flowOf(
            SendMessage(
                chat_id = message.chat.id.toString(),
                text = """
                User id: ${contact?.user_id}
                Name: ${contact?.first_name} ${contact?.last_name}
            """.trimIndent()
            )
        )
    }

    companion object {
        private val INFO = CommandInfo(
            name = "!contact-info",
            context = Context.USER_CHAT,
            access = Access.ADMIN
        )
    }
}

class SpamCommand(
    override val info: Info = INFO
) : Command {
    override fun execute(
        message: Message,
        update: Update,
        args: List<String>
    ): Flow<Method<*>> {
        message.reply_to_message?.let { reply ->
            return flowOf(
                reply.delete,
                reply.banFrom
            )
        }

        return emptyFlow()
    }

    companion object {
        private val INFO = CommandInfo(
            name = "!spam",
            context = Context.GROUP_CHAT,
            access = Access.ADMIN
        )
    }
}

//class UpdateTitleCommand(
//    private val groups: FamilyConfiguration,
//    override val info: Info = INFO
//) : Command {
//    override fun execute(update: Update, args: List<String>): Flow<Action> {
//        val updates = groups.ids.flatMap { group ->
//            groups.admins.map { admin ->
//                SetChatAdministratorCustomTitleAction(
//                    chatId = group,
//                    title = admin.title,
//                    userId = admin.id.toInt()
//                )
//            }
//        }
//
//        return updates.asFlow()
//    }
//
//    companion object {
//        private val INFO = CommandInfo(
//            name = "!update-title",
//            context = Context.USER_CHAT,
//            access = Access.ADMIN
//        )
//    }
//}
