/*
 * This file is generated by jOOQ.
 */
package io.heapy.kotbot.database.routines


import io.heapy.kotbot.database.Public

import java.util.UUID

import org.jooq.Parameter
import org.jooq.impl.AbstractRoutine
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class UuidNsUrl : AbstractRoutine<UUID>("uuid_ns_url", Public.PUBLIC, SQLDataType.UUID) {
    companion object {

        /**
         * The parameter <code>public.uuid_ns_url.RETURN_VALUE</code>.
         */
        val RETURN_VALUE: Parameter<UUID?> = Internal.createParameter("RETURN_VALUE", SQLDataType.UUID, false, false)
    }

    init {
        returnParameter = UuidNsUrl.RETURN_VALUE
    }
}
