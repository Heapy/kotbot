package io.heapy.kotbot.infra.web

import io.heapy.komok.tech.di.lib.Module

@Module
class ServerModule(
    private val routesModule: RoutesModule,
) {
    val server by lazy {
        KtorServer(
            routes = routesModule.routes,
        )
    }
}
