package io.heapy.kotbot.apiparser

import io.heapy.kotbot.apiparser.model.ArrayApiType
import io.heapy.kotbot.apiparser.model.BooleanApiType
import io.heapy.kotbot.apiparser.model.ReferenceApiType
import org.jsoup.Jsoup
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ReturnTypeExtractorTest {
    @Test
    fun `prefers explicit on success return type over prose capitals`() {
        val type = extractType(
            "Returns the amount of Telegram Stars owned by a managed business account. " +
                "Requires the <em>can_view_gifts_and_stars</em> business bot right. " +
                "Returns <a href=\"#staramount\">StarAmount</a> on success."
        )

        assertEquals(ReferenceApiType(reference = "StarAmount"), type)
    }

    @Test
    fun `extracts true return type`() {
        val type = extractType("Returns <em>True</em> on success.")

        assertEquals(BooleanApiType(default = true), type)
    }

    @Test
    fun `extracts array return type`() {
        val type = extractType("Returns an Array of <a href=\"#update\">Update</a> objects.")

        assertEquals(
            ArrayApiType(array = ReferenceApiType(reference = "Update")),
            type
        )
    }

    @Test
    fun `extracts on success sentence return type`() {
        val type = extractType("A method to get the current balance. On success, returns a StarAmount object.")

        assertEquals(ReferenceApiType(reference = "StarAmount"), type)
    }

    private fun extractType(descriptionHtml: String) =
        ReturnTypeExtractor.extract(listOf(Jsoup.parse("<p>$descriptionHtml</p>").selectFirst("p")!!))
}
