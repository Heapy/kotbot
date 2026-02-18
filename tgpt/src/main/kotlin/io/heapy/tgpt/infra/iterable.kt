package io.heapy.tgpt.infra

inline fun <T> Iterable<T>.withEach(action: T.() -> Unit) {
    for (element in this) action(element)
}
