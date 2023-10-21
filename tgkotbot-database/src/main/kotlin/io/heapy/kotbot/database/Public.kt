/*
 * This file is generated by jOOQ.
 */
package io.heapy.kotbot.database


import io.heapy.kotbot.database.tables.UpdateRaw

import kotlin.collections.List

import org.jooq.Catalog
import org.jooq.Table
import org.jooq.impl.SchemaImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class Public : SchemaImpl("public", DefaultCatalog.DEFAULT_CATALOG) {
    public companion object {

        /**
         * The reference instance of <code>public</code>
         */
        val PUBLIC: Public = Public()
    }

    /**
     * The table <code>public.update_raw</code>.
     */
    val UPDATE_RAW: UpdateRaw get() = UpdateRaw.UPDATE_RAW

    override fun getCatalog(): Catalog = DefaultCatalog.DEFAULT_CATALOG

    override fun getTables(): List<Table<*>> = listOf(
        UpdateRaw.UPDATE_RAW
    )
}
