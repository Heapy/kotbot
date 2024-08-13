package io.heapy.kotbot.bot.rules

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.infra.HttpClientModule
import io.heapy.kotbot.infra.configuration.CasConfiguration
import io.heapy.komok.tech.config.ConfigurationModule

@Module
open class RulesModule(
    private val configurationModule: ConfigurationModule,
    private val httpClientModule: HttpClientModule,
) {
    open val rules by lazy {
        listOf(
            deleteJoinRule,
            deleteSpamRule,
            deleteHelloRule,
            longTimeNoSeeRule,
            kasperskyCareersRule,
            deleteSwearingRule,
            deleteVoiceMessageRule,
            deleteVideoNoteMessageRule,
            deleteStickersRule,
            combotCasRule,
            deletePropagandaRule,
        )
    }

    open val deleteJoinRule: Rule by lazy(::DeleteJoinRule)

    open val deleteSpamRule: Rule by lazy(::DeleteSpamRule)

    open val deleteHelloRule: Rule by lazy(::DeleteHelloRule)

    open val longTimeNoSeeRule: Rule by lazy(::LongTimeNoSeeRule)

    open val kasperskyCareersRule: Rule by lazy(::KasperskyCareersRule)

    open val deleteSwearingRule: Rule by lazy(::DeleteSwearingRule)

    open val deleteVoiceMessageRule: Rule by lazy(::DeleteVoiceMessageRule)

    open val deleteVideoNoteMessageRule: Rule by lazy(::DeleteVideoNoteRule)

    open val deleteStickersRule: Rule by lazy(::DeleteStickersRule)

    open val deletePropagandaRule: Rule by lazy(::DeletePropagandaRule)

    open val combotCasRule: Rule by lazy {
        CombotCasRule(
            client = httpClientModule.httpClient,
            casConfiguration = casConfiguration,
        )
    }

    open val casConfiguration: CasConfiguration by lazy {
        configurationModule
            .config
            .read(
                deserializer = CasConfiguration.serializer(),
                path = "cas",
            )
    }
}
