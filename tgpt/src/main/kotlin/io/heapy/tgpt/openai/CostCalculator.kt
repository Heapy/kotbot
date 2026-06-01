package io.heapy.tgpt.openai

import java.math.BigDecimal
import java.math.RoundingMode

object CostCalculator {
    // Approximate pricing per 1M tokens (input/output)
    private val MODEL_PRICING: Map<String, Pair<BigDecimal, BigDecimal>> = mapOf(
        "gpt-5.5" to (BigDecimal("5.00") to BigDecimal("30.00")),
    )

    private val DEFAULT_PRICING = BigDecimal("2.50") to BigDecimal("10.00")

    fun estimateCost(
        model: String,
        inputTokens: Int,
        outputTokens: Int,
    ): BigDecimal {
        val (inputPrice, outputPrice) = MODEL_PRICING[model] ?: DEFAULT_PRICING
        val inputCost = inputPrice.multiply(BigDecimal(inputTokens)).divide(BigDecimal(1_000_000), 6, RoundingMode.HALF_UP)
        val outputCost = outputPrice.multiply(BigDecimal(outputTokens)).divide(BigDecimal(1_000_000), 6, RoundingMode.HALF_UP)
        return inputCost.add(outputCost)
    }
}
