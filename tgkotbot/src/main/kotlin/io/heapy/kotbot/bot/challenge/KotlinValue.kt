package io.heapy.kotbot.bot.challenge

sealed interface KotlinValue {
    fun display(): String
}

data class IntValue(val value: Int) : KotlinValue {
    override fun display(): String = value.toString()
}

data class StringValue(val value: String) : KotlinValue {
    override fun display(): String = value
}

data class BooleanValue(val value: Boolean) : KotlinValue {
    override fun display(): String = value.toString()
}

data class ListValue(val elements: List<KotlinValue>) : KotlinValue {
    override fun display(): String = "[${elements.joinToString(", ") { it.display() }}]"
}

data class MapValue(val entries: Map<KotlinValue, KotlinValue>) : KotlinValue {
    override fun display(): String = "{${entries.entries.joinToString(", ") { "${it.key.display()}=${it.value.display()}" }}}"
}

data class PairValue(val first: KotlinValue, val second: KotlinValue) : KotlinValue {
    override fun display(): String = "(${first.display()}, ${second.display()})"
}

data object NullValue : KotlinValue {
    override fun display(): String = "null"
}
