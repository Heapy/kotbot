/*
 * This file is generated by jOOQ.
 */
package io.heapy.kotbot.database.tables.records


import io.heapy.kotbot.database.enums.TelegramUserRole
import io.heapy.kotbot.database.enums.TelegramUserStatus
import io.heapy.kotbot.database.tables.TelegramUser
import io.heapy.kotbot.database.tables.interfaces.ITelegramUser

import java.time.LocalDateTime

import org.jooq.Record1
import org.jooq.impl.UpdatableRecordImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class TelegramUserRecord private constructor() : UpdatableRecordImpl<TelegramUserRecord>(TelegramUser.TELEGRAM_USER), ITelegramUser {

    open override var internalId: Long?
        set(value): Unit = set(0, value)
        get(): Long? = get(0) as Long?

    open override var telegramId: Long
        set(value): Unit = set(1, value)
        get(): Long = get(1) as Long

    open override var created: LocalDateTime?
        set(value): Unit = set(2, value)
        get(): LocalDateTime? = get(2) as LocalDateTime?

    open override var lastMessage: LocalDateTime?
        set(value): Unit = set(3, value)
        get(): LocalDateTime? = get(3) as LocalDateTime?

    open override var messageCount: Int?
        set(value): Unit = set(4, value)
        get(): Int? = get(4) as Int?

    open override var status: TelegramUserStatus?
        set(value): Unit = set(5, value)
        get(): TelegramUserStatus? = get(5) as TelegramUserStatus?

    open override var role: TelegramUserRole?
        set(value): Unit = set(6, value)
        get(): TelegramUserRole? = get(6) as TelegramUserRole?

    open override var badge: String?
        set(value): Unit = set(7, value)
        get(): String? = get(7) as String?

    open override var version: Int?
        set(value): Unit = set(8, value)
        get(): Int? = get(8) as Int?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<Long?> = super.key() as Record1<Long?>

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    fun from(from: ITelegramUser) {
        this.internalId = from.internalId
        this.telegramId = from.telegramId
        this.created = from.created
        this.lastMessage = from.lastMessage
        this.messageCount = from.messageCount
        this.status = from.status
        this.role = from.role
        this.badge = from.badge
        this.version = from.version
        resetChangedOnNotNull()
    }

    /**
     * Create a detached, initialised TelegramUserRecord
     */
    constructor(internalId: Long? = null, telegramId: Long, created: LocalDateTime? = null, lastMessage: LocalDateTime? = null, messageCount: Int? = null, status: TelegramUserStatus? = null, role: TelegramUserRole? = null, badge: String? = null, version: Int? = null): this() {
        this.internalId = internalId
        this.telegramId = telegramId
        this.created = created
        this.lastMessage = lastMessage
        this.messageCount = messageCount
        this.status = status
        this.role = role
        this.badge = badge
        this.version = version
        resetChangedOnNotNull()
    }

    /**
     * Create a detached, initialised TelegramUserRecord
     */
    constructor(value: io.heapy.kotbot.database.tables.pojos.TelegramUser?): this() {
        if (value != null) {
            this.internalId = value.internalId
            this.telegramId = value.telegramId
            this.created = value.created
            this.lastMessage = value.lastMessage
            this.messageCount = value.messageCount
            this.status = value.status
            this.role = value.role
            this.badge = value.badge
            this.version = value.version
            resetChangedOnNotNull()
        }
    }
}
