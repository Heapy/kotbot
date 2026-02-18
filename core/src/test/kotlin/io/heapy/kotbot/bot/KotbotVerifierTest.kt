package io.heapy.kotbot.bot

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class KotbotVerifierTest {
    @Test
    @WithKotbotVerifier
    fun `verifier is injected correctly`(
        kotbotVerifier: KotbotVerifier
    ) {
        assertNotNull(kotbotVerifier, "Verifier should be injected")
    }
}