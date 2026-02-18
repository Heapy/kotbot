package io.heapy.tgpt.infra.lifecycle

import io.heapy.komok.tech.di.lib.Module
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Module
open class ApplicationScopeModule(
    private val autoClosableModule: AutoClosableModule,
) {
    open val applicationJob by lazy {
        autoClosableModule.addClosable(
            SupervisorJob(),
        ) { job ->
            job.cancel()
        }
    }

    open val applicationScope by lazy {
        CoroutineScope(applicationJob + Dispatchers.Default + CoroutineName("ApplicationScope"))
    }
}
