package io.heapy.kotbot.infra.lifecycle

import io.heapy.komok.tech.di.lib.Module
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

@Module
open class ApplicationScopeModule {
    open val applicationJob by lazy {
        Job()
    }

    open val applicationScope by lazy {
        CoroutineScope(applicationJob + Dispatchers.Default + CoroutineName("ApplicationScope"))
    }
}
