/*
 * This file is generated by jOOQ.
 */
package io.heapy.kotbot.database.tables.interfaces


import java.io.Serializable
import java.time.LocalDateTime

import org.jooq.JSONB


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
interface IUpdateRaw : Serializable {
    val id: Long?
    val created: LocalDateTime
    val update: JSONB
}
