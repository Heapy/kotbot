package io.heapy.kotbot

import io.heapy.kotbot.Command.Access.ADMIN
import io.heapy.kotbot.Command.Access.USER
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class CommandAccessTests {
    @Test
    fun `test allowed`() {
        assertAll(
            { assertFalse(ADMIN.isAllowed(USER)) },
            { assertTrue(ADMIN.isAllowed(ADMIN)) },
            { assertTrue(USER.isAllowed(ADMIN)) },
            { assertTrue(USER.isAllowed(USER)) },
        )
    }
}
