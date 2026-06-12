package io.heapy.kotbot.bot

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@WithKotbotVerifier
class KotbotVerifierTest {
    @Test
    fun `verifier is injected correctly`(
        kotbotVerifier: KotbotVerifier
    ) {
        assertNotNull(kotbotVerifier, "Verifier should be injected")
    }
}
