import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ParseTest {
    @Test
    fun `processVersion preserves image alt text`() {
        val output = processVersion(
            """
            <div id="dev_page_content">
                <p><strong><img class="emoji" alt="⚠️" /> WARNING</strong></p>
                <table>
                    <tr>
                        <th>Field</th>
                        <th>Description</th>
                    </tr>
                    <tr>
                        <td>value</td>
                        <td>Value for “<img class="emoji" alt="🎲" />” base emoji</td>
                    </tr>
                </table>
            </div>
            """.trimIndent()
        )

        assertTrue(output.contains("⚠️ WARNING"))
        assertTrue(output.contains("Value for “🎲” base emoji"))
    }
}
