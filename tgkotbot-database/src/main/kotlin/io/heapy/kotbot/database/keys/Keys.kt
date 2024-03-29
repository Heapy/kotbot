/*
 * This file is generated by jOOQ.
 */
package io.heapy.kotbot.database.keys


import io.heapy.kotbot.database.tables.UpdateRaw
import io.heapy.kotbot.database.tables.records.UpdateRawRecord

import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal



// -------------------------------------------------------------------------
// UNIQUE and PRIMARY KEY definitions
// -------------------------------------------------------------------------

val UPDATE_RAW_PK: UniqueKey<UpdateRawRecord> = Internal.createUniqueKey(UpdateRaw.UPDATE_RAW, DSL.name("update_raw_pk"), arrayOf(UpdateRaw.UPDATE_RAW.ID), true)
