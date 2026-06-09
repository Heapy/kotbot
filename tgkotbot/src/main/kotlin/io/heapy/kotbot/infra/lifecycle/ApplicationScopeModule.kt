package io.heapy.kotbot.infra.lifecycle

import io.heapy.komok.tech.di.lib.Module
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Module
class ApplicationScopeModule(
    private val autoClosableModule: AutoClosableModule,
) {
    val applicationJob by lazy {
        autoClosableModule.addClosable(
            SupervisorJob(),
        ) { job ->
            job.cancel()
        }
    }

    val applicationScope by lazy {
        CoroutineScope(applicationJob + Dispatchers.Default + CoroutineName("ApplicationScope"))
    }
}
