package io.heapy.kotbot.bot

import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.parallel.ResourceLock

/**
 * Annotation to inject a [KotbotVerifier] instance into test methods.
 */
@ResourceLock("kotbot")
@ExtendWith(KotbotVerifierParameterResolver::class)
annotation class WithKotbotVerifier
