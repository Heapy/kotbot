package io.heapy.kotbot.bot.testing

import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi

@OptIn(ExperimentalAtomicApi::class)
class TestingCasClientToggle {
    private val enabled = AtomicBoolean(true)

    fun toggle() {
        val _ = enabled.exchange(!enabled.load())
    }

    fun get() = enabled.load()
}
