package `in`.sartaj.fb2site.writer

import `in`.sartaj.fb2site.model.ConvertOptions
import `in`.sartaj.fb2site.model.Post
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.nio.file.Files
import java.time.Instant
import kotlin.io.path.createTempDirectory
import kotlin.io.path.readText

class HtmlWriterTest {

    @Test
    fun testWritePostGeneratesHtml() {
        val tempDir = createTempDirectory("fb2site_test")
        val exportDir = createTempDirectory("export_dir")

        val options = ConvertOptions(outputDir = tempDir)
        val post = Post(
            id = "test-id",
            timestamp = Instant.parse("2024-01-01T12:00:00Z"),
            title = "Test Post",
            body = "This is a test post.\n\nWith multiple paragraphs.",
            links = listOf("https://example.com"),
            language = "en"
        )

        HtmlWriter.writePost(post, exportDir, options)

        val outputDir = tempDir.resolve(options.contentDir)
        val outputFile = outputDir.resolve("2024-01-01-120000.html")

        assertTrue(Files.exists(outputFile), "Output file should exist")

        val content = outputFile.readText()

        assertTrue(content.contains("title: \"Test Post\""))
        assertTrue(content.contains("date: 2024-01-01T12:00:00Z"))
        assertTrue(content.contains("lang: en"))
        assertTrue(content.contains("<p>This is a test post.</p>"))
        assertTrue(content.contains("<p>With multiple paragraphs.</p>"))
        assertTrue(content.contains("<ul class=\"links\">"))
        assertTrue(content.contains("<li><a href=\"https://example.com\">https://example.com</a></li>"))
    }
}
