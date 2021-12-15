package io.heapy.kotbot

import org.slf4j.Logger
import org.slf4j.LoggerFactory

internal inline fun <reified T : Any> logger(): Logger =
    LoggerFactory.getLogger(T::class.java)
