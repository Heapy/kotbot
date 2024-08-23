package io.heapy.kotbot.bot.rules

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.*
import io.heapy.kotbot.bot.model.Update

/**
 * In @kotlin_lang following Send Media permissions disabled:
 *
 * - Send Videos
 * - Send Stickers & GIFs
 * - Send Music
 * - Send Voice Messages
 * - Send Video Messages
 * - Send Polls
 *
 * So a few rules above should never be triggered.
 */
class DeleteMessageRule : Rule {
    override suspend fun validate(
        kotbot: Kotbot,
        update: Update,
        actions: Actions,
    ) {
        update.anyMessage?.let { message ->
            when {
                message.new_chat_members != null -> {
                    actions.runIfNew("new_chat_members_rule", message.delete) { deleteMessage ->
                        log.info("Delete joined users message ${message.new_chat_members}")
                        kotbot.executeSafely(deleteMessage)
                    }
                }

                message.left_chat_member != null -> {
                    actions.runIfNew("left_chat_member_rule", message.delete) { deleteMessage ->
                        log.info("Delete left user message ${message.left_chat_member}")
                        kotbot.executeSafely(deleteMessage)
                    }
                }

                message.video_note != null -> {
                    actions.runIfNew("video_note_rule", message.delete) {
                        log.info("Delete video note message from ${message.from?.info}.")
                        kotbot.executeSafely(it)
                    }
                }

                message.voice != null -> {
                    actions.runIfNew("voice_rule", message.delete) {
                        log.info("Delete voice message from ${message.from?.info}.")
                        kotbot.executeSafely(it)
                    }
                }

                message.sticker != null -> {
                    actions.runIfNew("sticker_rule", message.delete) {
                        log.info("Delete sticker-message from ${message.from?.info}.")
                        kotbot.executeSafely(it)
                    }
                }
            }
        }
    }

    private companion object : Logger()
}
