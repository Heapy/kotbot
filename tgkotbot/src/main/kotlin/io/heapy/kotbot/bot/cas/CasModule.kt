package io.heapy.kotbot.bot.cas

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.bot.testing.KotbotTestingModule
import io.heapy.kotbot.infra.HttpClientModule

@Module
class CasModule(
    private val httpClientModule: HttpClientModule,
    private val kotbotTestingModule: KotbotTestingModule,
) {
    val casClient: CasClient by lazy {
        kotbotTestingModule.casTestingClient(
            DefaultCasClient(
                client = httpClientModule.httpClient,
            ),
        )
    }
}
