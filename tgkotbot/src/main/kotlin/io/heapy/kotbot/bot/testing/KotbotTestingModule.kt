package io.heapy.kotbot.bot.testing

import io.heapy.komok.tech.config.ConfigurationModule
import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.bot.cas.CasClient
import io.heapy.kotbot.bot.cas.TestingCasClient
import io.heapy.kotbot.infra.configuration.TestingConfiguration

@Module
class KotbotTestingModule(
    private val configurationModule: ConfigurationModule,
) {
    val testingCasClientToggle by lazy {
        TestingCasClientToggle()
    }

    val casTestingClient: (CasClient) -> CasClient by lazy {
        { casClient: CasClient ->
            TestingCasClient(
                testingConfiguration = testingConfiguration,
                testingCasClientToggle = testingCasClientToggle,
                casClient = casClient,
            )
        }
    }

    val testingConfiguration: TestingConfiguration by lazy {
        configurationModule
            .config
            .read(
                deserializer = TestingConfiguration.serializer(),
                path = "testing",
            )
    }
}
