package io.heapy.kotbot.bot

private class SampleLongPollingKotBot : LongPollingKotBot {
    override suspend fun handleUpdate(update: ApiUpdate) {
        println(update)
    }
}

