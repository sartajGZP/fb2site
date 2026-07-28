package `in`.sartaj.fb2site.writer

import `in`.sartaj.fb2site.model.ConvertOptions
import `in`.sartaj.fb2site.model.Post
import java.io.BufferedWriter
import java.nio.file.Files
import java.nio.file.Path
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object MarkdownWriter {

    private val fileFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss")
            .withZone(ZoneOffset.UTC)

    fun writePost(
        post: Post,
        exportDir: Path,
        options: ConvertOptions,
    ) {
        val outputFile = createOutputFile(post, options)

        Files.newBufferedWriter(outputFile).use { writer ->
            writeFrontMatter(writer, post, options)
            writeBody(writer, post)
            writeLinks(writer, post)
            writePhotos(writer, post, exportDir, options)
            writeVideos(writer, post, exportDir, options)
        }
    }

    private fun createOutputFile(
        post: Post,
        options: ConvertOptions,
    ): Path {

        val contentDir =
            options.outputDir.resolve(options.contentDir)

        Files.createDirectories(contentDir)

        val filename =
            fileFormatter.format(post.timestamp) + ".md"

        return contentDir.resolve(filename)
    }

    private fun writeFrontMatter(
        writer: BufferedWriter,
        post: Post,
        options: ConvertOptions,
    ) {
        TODO()
    }

    private fun writeBody(
        writer: BufferedWriter,
        post: Post,
    ) {
        TODO()
    }

    private fun writeLinks(
        writer: BufferedWriter,
        post: Post,
    ) {
        TODO()
    }

    private fun writePhotos(
        writer: BufferedWriter,
        post: Post,
        exportDir: Path,
        options: ConvertOptions,
    ) {
        TODO()
    }

    private fun writeVideos(
        writer: BufferedWriter,
        post: Post,
        exportDir: Path,
        options: ConvertOptions,
    ) {
        TODO()
    }
}
