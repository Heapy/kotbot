# kotbot [![Build](https://github.com/Heapy/kotbot/actions/workflows/build.yml/badge.svg)](https://github.com/Heapy/kotbot/actions/workflows/build.yml)

* [Core](https://github.com/Heapy/kotbot/tree/main/core#readme) – Lightweight, opinionated library
  implementing [telegram bot api](https://core.telegram.org/bots)
* [Tgkotbot](https://github.com/Heapy/kotbot/tree/main/tgkotbot#readme) – Bot implementation for needs
  of [Kotlin Community](https://t.me/kotlin_lang) in the telegram

## Bot API 8.3

### Install the library

```kotlin
implementation("io.heapy.kotbot:core:1.4.0")
```

### Example

Execute a single method:

```kotlin
suspend fun main() {
    val kotbot = Kotbot(
        token = System.getenv("KOTBOT_TOKEN"),
    )

  kotbot.execute(GetMe())
      .also(::println)
}
```

Subscribe for updates:

```kotlin
suspend fun main() {
    val kotbot = Kotbot(
        token = System.getenv("KOTBOT_TOKEN"),
    )

    // Flow, which emits updates
    kotbot.receiveUpdates()
        .collect(::println)
}
```
