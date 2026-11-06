package io.heapy.tgpt.openai

import java.math.BigDecimal
import java.math.RoundingMode

object CostCalculator {
    // Approximate Standard pricing per 1M tokens (input/output), excluding caching and tools.
    // https://developers.openai.com/api/docs/pricing
    private val MODEL_PRICING: Map<String, Pair<BigDecimal, BigDecimal>> = mapOf(
        "gpt-6.1-sol" to (BigDecimal("2.00") to BigDecimal("10.00")),
        "gpt-5.5" to (BigDecimal("5.00") to BigDecimal("30.00")),
    )

    private val DEFAULT_PRICING = BigDecimal("2.50") to BigDecimal("10.00")

    fun estimateCost(
        model: String,
        inputTokens: Int,
        outputTokens: Int,
    ): BigDecimal {
        val [baseInputPrice, baseOutputPrice] = MODEL_PRICING[model] ?: DEFAULT_PRICING
        val longContext = model == "gpt-6.1-sol" && inputTokens > 272_000
        val inputPrice = if (longContext) baseInputPrice * BigDecimal("2") else baseInputPrice
        val outputPrice = if (longContext) baseOutputPrice * BigDecimal("1.5") else baseOutputPrice
        val inputCost = inputPrice.multiply(BigDecimal(inputTokens)).divide(BigDecimal(1_000_000), 6, RoundingMode.HALF_UP)
        val outputCost = outputPrice.multiply(BigDecimal(outputTokens)).divide(BigDecimal(1_000_000), 6, RoundingMode.HALF_UP)
        return inputCost.add(outputCost)
    }
}
