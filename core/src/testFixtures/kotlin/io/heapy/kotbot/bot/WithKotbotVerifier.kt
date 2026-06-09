package io.heapy.kotbot.bot

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.parallel.ResourceLock

/**
 * Annotation to inject a [KotbotVerifier] instance into test methods.
 *
 * Verifier tests drive the real Telegram Bot API and block on manual PASS/FAIL input, so they are
 * tagged `ManualTest` and excluded from the normal `test` run. Only one `getUpdates` consumer is
 * allowed per bot token, so they must never run concurrently with each other or across modules.
 */
@Tag("ManualTest")
@ResourceLock("kotbot")
@ExtendWith(KotbotVerifierParameterResolver::class)
annotation class WithKotbotVerifier
