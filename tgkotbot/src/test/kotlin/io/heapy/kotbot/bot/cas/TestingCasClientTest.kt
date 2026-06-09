package io.heapy.kotbot.bot.cas

import io.heapy.kotbot.bot.testing.TestingCasClientToggle
import io.heapy.kotbot.infra.configuration.TestingConfiguration
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestingCasClientTest {
    private class RecordingCasClient(
        private val result: CasResult,
    ) : CasClient {
        var calls = 0
            private set

        override suspend fun check(userId: Long): CasResult {
            calls++
            return result
        }
    }

    private fun client(
        delegate: RecordingCasClient,
        testUserId: Long? = 42L,
        toggle: TestingCasClientToggle = TestingCasClientToggle(),
    ) = TestingCasClient(
        testingConfiguration = TestingConfiguration(testUserId = testUserId),
        casClient = delegate,
        testingCasClientToggle = toggle,
    )

    @Test
    fun `test user is force-flagged without hitting the delegate`() = runBlocking {
        val delegate = RecordingCasClient(CasResult.Clean)
        val result = client(delegate).check(42L)
        assertEquals(
            CasResult.Flagged(
                offenses = null,
                timeAdded = null,
                messages = listOf("Forced CAS flag via configuration (testing.testUserId)"),
            ),
            result,
        )
        assertEquals(0, delegate.calls)
    }

    @Test
    fun `toggling the test user verdict yields clean, still without the delegate`() = runBlocking {
        val delegate = RecordingCasClient(CasResult.Clean)
        val toggle = TestingCasClientToggle()
        val client = client(delegate, toggle = toggle)

        toggle.toggle() // flagged -> clean
        assertEquals(CasResult.Clean, client.check(42L))
        assertEquals(0, delegate.calls)
    }

    @Test
    fun `non-test users pass through to the delegate`() = runBlocking {
        val delegate = RecordingCasClient(CasResult.Flagged(offenses = 1, timeAdded = null))
        val result = client(delegate).check(99L)
        assertEquals(CasResult.Flagged(offenses = 1, timeAdded = null), result)
        assertEquals(1, delegate.calls)
    }

    @Test
    fun `with no test user configured everyone passes through`() = runBlocking {
        val delegate = RecordingCasClient(CasResult.Clean)
        val result = client(delegate, testUserId = null).check(42L)
        assertEquals(CasResult.Clean, result)
        assertEquals(1, delegate.calls)
    }
}
