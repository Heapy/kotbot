package io.heapy.kotbot.bot

public interface LongPollingKotBot {
    public suspend fun handleUpdate(update: ApiUpdate)
}
