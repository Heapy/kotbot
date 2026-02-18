package io.heapy.kotbot.bot

import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KotbotVerifierMultipleTestsTest {
    
    private lateinit var firstInstance: KotbotVerifier
    
    @Test
    @Order(1)
    @WithKotbotVerifier
    fun `first test should get verifier instance`(
        kotbotVerifier: KotbotVerifier
    ) {
        firstInstance = kotbotVerifier
    }
    
    @Test
    @Order(2)
    @WithKotbotVerifier
    fun `second test should get same verifier instance`(
        kotbotVerifier: KotbotVerifier
    ) {
        assertSame(firstInstance, kotbotVerifier, "Should be the same KotbotVerifier instance")
    }
}