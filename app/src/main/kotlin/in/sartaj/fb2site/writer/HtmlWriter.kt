package `in`.sartaj.fb2site.writer

import `in`.sartaj.fb2site.model.ConvertOptions
import `in`.sartaj.fb2site.model.NormalisedPost
import `in`.sartaj.fb2site.writer.html.BodyWriter
import `in`.sartaj.fb2site.writer.html.FrontMatterWriter
import `in`.sartaj.fb2site.writer.html.LinksWriter
import `in`.sartaj.fb2site.writer.html.MediaWriter
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object HtmlWriter {

    private val yearFormatter = DateTimeFormatter.ofPattern("yyyy").withZone(ZoneOffset.UTC)
    private val monthFormatter = DateTimeFormatter.ofPattern("MM").withZone(ZoneOffset.UTC)

    fun writePost(
        post: NormalisedPost,
        exportDir: Path,
        options: ConvertOptions,
    ) {
        val outputFile = createOutputFile(post, options)
        println("Writing to: $outputFile")

        Files.newBufferedWriter(outputFile).use { writer ->
            FrontMatterWriter.write(writer, post, options)
            BodyWriter.write(writer, post)
            LinksWriter.write(writer, post)
            MediaWriter.writePhotos(writer, post, exportDir, options)
            MediaWriter.writeVideos(writer, post, exportDir, options)
        }
    }

    private fun createOutputFile(
        post: NormalisedPost,
        options: ConvertOptions,
    ): Path {
        val year = yearFormatter.format(post.timestamp)
        val month = monthFormatter.format(post.timestamp)

        val targetDir = options.outputDir
            .resolve(options.contentDir)
            .resolve(year)
            .resolve(month)

        Files.createDirectories(targetDir)

        val baseName = post.id?.takeUnless { it.isBlank() } ?: sha256(post.body)
        var outputFile = targetDir.resolve("$baseName.html")
        var counter = 2

        while (Files.exists(outputFile)) {
            outputFile = targetDir.resolve("$baseName-$counter.html")
            counter++
        }

        return outputFile
    }

    private fun sha256(text: String): String {
        val bytes = MessageDigest
            .getInstance("SHA-256")
            .digest(text.toByteArray())

        return bytes.joinToString("") {
            "%02x".format(it)
        }
    }
}
