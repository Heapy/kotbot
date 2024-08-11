package io.heapy.kotbot.infra.web

import io.heapy.komok.tech.di.lib.Module

@Module
open class ServerModule(
    private val routesModule: RoutesModule,
) {
    open val server by lazy {
        KtorServer(
            routes = routesModule.routes,
        )
    }
}
