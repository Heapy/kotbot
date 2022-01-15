package io.heapy.kotbot

import org.slf4j.Logger
import org.slf4j.LoggerFactory

internal inline fun <reified T : Any> logger(): Logger =
    LoggerFactory.getLogger(T::class.java)

internal fun readResource(resource: String): String? =
    ApplicationFactory::class
        .java
        .classLoader
        .getResource(resource)
        ?.readText()
