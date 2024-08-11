package io.heapy.kotbot.infra

import io.heapy.kotbot.bot.ApplicationModule

internal fun readResource(resource: String): String? =
    ApplicationModule::class
        .java
        .classLoader
        .getResource(resource)
        ?.readText()
