package io.heapy.kotbot.bot.commands

import io.heapy.kotbot.bot.commands.Command.Access.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class CommandAccessTests {
    @Test
    fun `test allowed`() {
        assertAll(
            { assertFalse(ADMIN.isAllowed(USER)) },
            { assertFalse(ADMIN.isAllowed(MODERATOR)) },
            { assertTrue(ADMIN.isAllowed(ADMIN)) },
            { assertFalse(MODERATOR.isAllowed(USER)) },
            { assertTrue(MODERATOR.isAllowed(ADMIN)) },
            { assertTrue(MODERATOR.isAllowed(MODERATOR)) },
            { assertTrue(USER.isAllowed(ADMIN)) },
            { assertTrue(USER.isAllowed(MODERATOR)) },
            { assertTrue(USER.isAllowed(USER)) },
        )
    }
}
