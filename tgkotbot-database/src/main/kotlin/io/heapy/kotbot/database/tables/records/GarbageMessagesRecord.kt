/*
 * This file is generated by jOOQ.
 */
package io.heapy.kotbot.database.tables.records


import io.heapy.kotbot.database.enums.ActionType
import io.heapy.kotbot.database.enums.MatchType
import io.heapy.kotbot.database.tables.GarbageMessages
import io.heapy.kotbot.database.tables.interfaces.IGarbageMessages

import org.jooq.Record1
import org.jooq.impl.UpdatableRecordImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class GarbageMessagesRecord private constructor() : UpdatableRecordImpl<GarbageMessagesRecord>(GarbageMessages.GARBAGE_MESSAGES), IGarbageMessages {

    open override var id: Int?
        set(value): Unit = set(0, value)
        get(): Int? = get(0) as Int?

    open override var text: String
        set(value): Unit = set(1, value)
        get(): String = get(1) as String

    open override var type: String
        set(value): Unit = set(2, value)
        get(): String = get(2) as String

    open override var match: MatchType
        set(value): Unit = set(3, value)
        get(): MatchType = get(3) as MatchType

    open override var action: ActionType
        set(value): Unit = set(4, value)
        get(): ActionType = get(4) as ActionType

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<Int?> = super.key() as Record1<Int?>

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    fun from(from: IGarbageMessages) {
        this.id = from.id
        this.text = from.text
        this.type = from.type
        this.match = from.match
        this.action = from.action
        resetChangedOnNotNull()
    }

    /**
     * Create a detached, initialised GarbageMessagesRecord
     */
    constructor(id: Int? = null, text: String, type: String, match: MatchType, action: ActionType): this() {
        this.id = id
        this.text = text
        this.type = type
        this.match = match
        this.action = action
        resetChangedOnNotNull()
    }

    /**
     * Create a detached, initialised GarbageMessagesRecord
     */
    constructor(value: io.heapy.kotbot.database.tables.pojos.GarbageMessages?): this() {
        if (value != null) {
            this.id = value.id
            this.text = value.text
            this.type = value.type
            this.match = value.match
            this.action = value.action
            resetChangedOnNotNull()
        }
    }
}
