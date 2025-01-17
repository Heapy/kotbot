package io.heapy.kotbot.bot.rules

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class RulesTests {
    @Test
    fun `word regex tests`() {
        assertAll(
            { assertTrue("hello world".contains(wordRegex("hello"))) },
            { assertTrue("hello, world".contains(wordRegex("hello"))) },
            { assertTrue("Hello, world".contains(wordRegex("hello"))) },
            { assertTrue("Hello world".contains(wordRegex("hello"))) },
            { assertTrue("hello world".contains(wordRegex("world"))) },
            { assertTrue("hello World".contains(wordRegex("world"))) },
            { assertFalse("hello world".contains(wordRegex("worl"))) },
            { assertFalse("hello world".contains(wordRegex("orld"))) },
            { assertFalse("hello world".contains(wordRegex("orl"))) },
            { assertFalse("hello world".contains(wordRegex("hell"))) }
        )
    }
}
