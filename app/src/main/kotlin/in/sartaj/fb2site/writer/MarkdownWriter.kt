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
	println(outputFile.toAbsolutePath())
	println("Writing to: $outputFile")

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
    writer.write("---")
    writer.newLine()

    writer.write("""title: "${post.title.replace("\"", "\\\"")}"""")
    writer.newLine()

    writer.write("date: ${post.timestamp}")
    writer.newLine()

    if (post.author != null) {
        writer.write("""author: "${post.author}"""")
        writer.newLine()
    }

    if (post.facebookUrl != null) {
        writer.write("""facebook: "${post.facebookUrl}"""")
        writer.newLine()
    }

    if (post.group != null) {
        writer.write("""group: "${post.group}"""")
        writer.newLine()
    }

    writer.write("layout: ${options.layout}")
    writer.newLine()

    writer.write("lang: ${post.language}")
    writer.newLine()

    writer.write("tags:")
    writer.newLine()
    writer.write("  - facebook")
    writer.newLine()

    writer.write("---")
    writer.newLine()
    writer.newLine()
}

	private fun writeBody(
    writer: BufferedWriter,
    post: Post,
) {
    if (post.body.isBlank()) {
        return
    }

    writer.write(post.body)
    writer.newLine()
    writer.newLine()
}
   private fun writeLinks(
    writer: BufferedWriter,
    post: Post,
) {
    for (link in post.links) {
        writer.write("<$link>")
        writer.newLine()
    }

    if (post.links.isNotEmpty()) {
        writer.newLine()
    }
}
private fun writePhotos(
    writer: BufferedWriter,
    post: Post,
    exportDir: Path,
    options: ConvertOptions,
) {
    // TODO: Copy photos and write Markdown image links.
}
    private fun writeVideos(
        writer: BufferedWriter,
        post: Post,
        exportDir: Path,
        options: ConvertOptions,
    ) {
        //TODO()
    }
}
