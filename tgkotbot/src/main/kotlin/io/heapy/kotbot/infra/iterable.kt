package io.heapy.kotbot.infra

inline fun <T> Iterable<T>.withEach(action: T.() -> Unit) {
    for (element in this) action(element)
}
