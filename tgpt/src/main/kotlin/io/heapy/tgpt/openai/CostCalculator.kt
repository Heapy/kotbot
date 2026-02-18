package io.heapy.tgpt.openai

import java.math.BigDecimal
import java.math.RoundingMode

object CostCalculator {
    // Approximate pricing per 1M tokens (input/output)
    private val MODEL_PRICING: Map<String, Pair<BigDecimal, BigDecimal>> = mapOf(
        "gpt-4o" to (BigDecimal("2.50") to BigDecimal("10.00")),
        "gpt-4o-mini" to (BigDecimal("0.15") to BigDecimal("0.60")),
        "gpt-5.2" to (BigDecimal("2.50") to BigDecimal("10.00")),
    )

    private val DEFAULT_PRICING = BigDecimal("2.50") to BigDecimal("10.00")

    fun estimateCost(
        model: String,
        promptTokens: Int,
        completionTokens: Int,
    ): BigDecimal {
        val (inputPrice, outputPrice) = MODEL_PRICING[model] ?: DEFAULT_PRICING
        val inputCost = inputPrice.multiply(BigDecimal(promptTokens)).divide(BigDecimal(1_000_000), 6, RoundingMode.HALF_UP)
        val outputCost = outputPrice.multiply(BigDecimal(completionTokens)).divide(BigDecimal(1_000_000), 6, RoundingMode.HALF_UP)
        return inputCost.add(outputCost)
    }
}
