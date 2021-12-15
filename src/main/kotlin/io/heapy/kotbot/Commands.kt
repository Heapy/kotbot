package io.heapy.kotbot

import io.heapy.kotbot.Command.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.telegram.telegrambots.meta.api.objects.Update

public interface Command {
    public val info: Info

    public fun execute(
        update: Update,
        args: List<String>
    ): Flow<Action>

    public interface Info {
        public val name: String
        public val arity: Int
        public val context: Context
        public val access: Access
    }

    public enum class Context {
        USER_CHAT,
        GROUP_CHAT
    }

    public enum class Access {
        USER,
        ADMIN
    }
}

public data class CommandInfo(
    override val name: String,
    override val arity: Int = 0,
    override val context: Context = Context.USER_CHAT,
    override val access: Access = Access.USER
) : Info

public object NoopCommand : Command {
    override fun execute(update: Update, args: List<String>): Flow<Action> {
        return flowOf()
    }

    override val info: Info
        get() = INFO

    private val INFO = CommandInfo(
        name = "!noop",
        access = Access.ADMIN
    )
}

public class HelloWorldCommand(
    override val info: Info = INFO
) : Command {
    override fun execute(update: Update, args: List<String>): Flow<Action> {
        return flowOf(
            ReplyAction(
                chatId = update.message.chatId,
                message = "Hello, World!"
            )
        )
    }

    public companion object {
        private val INFO = CommandInfo(
            name = "!hello",
            access = Access.USER
        )
    }
}

public class ChatInfoCommand(
    override val info: Info = INFO
) : Command {
    override fun execute(update: Update, args: List<String>): Flow<Action> {
        return flowOf(
            ReplyAction(
                chatId = update.message.chatId,
                message = """
                Chat id: ${update.message.chatId}
            """.trimIndent()
            )
        )
    }

    public companion object {
        private val INFO = CommandInfo(
            name = "!chat-info",
            context = Context.GROUP_CHAT,
            access = Access.ADMIN
        )
    }
}

public class ContactInfoCommand(
    override val info: Info = INFO
) : Command {
    override fun execute(update: Update, args: List<String>): Flow<Action> {
        val contact = update.message?.replyToMessage?.contact
        return flowOf(
            ReplyAction(
                chatId = update.message.chatId,
                message = """
                User id: ${contact?.userId}
                Name: ${contact?.firstName} ${contact?.lastName}
            """.trimIndent()
            )
        )
    }

    public companion object {
        private val INFO = CommandInfo(
            name = "!contact-info",
            context = Context.USER_CHAT,
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
