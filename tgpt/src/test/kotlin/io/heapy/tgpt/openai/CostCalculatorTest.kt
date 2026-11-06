package io.heapy.tgpt.openai

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class CostCalculatorTest {
    @Test
    fun `Sol uses the published standard rates`() {
        assertEquals(BigDecimal("0.007000"), CostCalculator.estimateCost("gpt-6.1-sol", 1_000, 500))
    }

    @Test
    fun `Sol long context rates apply only above 272K input tokens`() {
        assertEquals(BigDecimal("0.554000"), CostCalculator.estimateCost("gpt-6.1-sol", 272_000, 1_000))
        assertEquals(BigDecimal("1.103004"), CostCalculator.estimateCost("gpt-6.1-sol", 272_001, 1_000))
    }

    @Test
    fun `historical GPT 5_5 estimates keep their original rates`() {
        assertEquals(BigDecimal("0.020000"), CostCalculator.estimateCost("gpt-5.5", 1_000, 500))
    }
}
