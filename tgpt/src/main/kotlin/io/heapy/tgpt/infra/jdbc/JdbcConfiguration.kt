package io.heapy.tgpt.infra.jdbc

import kotlinx.serialization.Serializable

@Serializable
data class JdbcConfiguration(
    val user: String,
    val password: String,
    val database: String,
    val host: String,
    val port: Int,
)
