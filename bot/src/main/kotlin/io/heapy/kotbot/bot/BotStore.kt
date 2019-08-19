package io.heapy.kotbot.bot

/**
 * Holds essential part of bot state, which should persist between restarts.
 */
interface BotStore {
    val families: MutableList<Family>
}

/**
 * Represents a family of chats. Chat family is a group of chats sharing policies, restrictions, admin list.
 * Family contains a list of corresponding [chatIds] and an [adminChatId].
 */
class Family(
    val chatIds: MutableList<Long>,
    val adminChatId: Long
)
