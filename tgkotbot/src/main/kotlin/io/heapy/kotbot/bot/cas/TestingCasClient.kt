package io.heapy.kotbot.bot.cas

import io.heapy.kotbot.bot.testing.TestingCasClientToggle
import io.heapy.kotbot.infra.configuration.TestingConfiguration

class TestingCasClient(
    private val testingConfiguration: TestingConfiguration,
    private val casClient: CasClient,
    private val testingCasClientToggle: TestingCasClientToggle,
) : CasClient {
    override suspend fun check(userId: Long): CasResult {
        return if (userId == testingConfiguration.testUserId) {
            if (testingCasClientToggle.get()) {
                CasResult.Flagged(
                    offenses = null,
                    timeAdded = null,
                    messages = listOf("Forced CAS flag via configuration (testing.testUserId)"),
                )
            } else {
                CasResult.Clean
            }
        } else {
            casClient
                .check(
                    userId = userId,
                )
        }
    }
}
