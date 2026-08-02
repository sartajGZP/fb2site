package `in`.sartaj.fb2site.writer

import `in`.sartaj.fb2site.model.ConvertOptions
import `in`.sartaj.fb2site.model.Post
import java.io.BufferedWriter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object HtmlWriter {

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
        if (post.body.isBlank()) return

        // Splitting paragraphs on double line-breaks and wrapping in <p> tags for 11ty
        post.body.trim().split(Regex("\r?\n\r?\n+")).forEach { paragraph ->
            val safeParagraph = escapeHtml(paragraph).replace("\n", "<br>")
            writer.write("<p>$safeParagraph</p>")
            writer.newLine()
        }
        writer.newLine()
    }

    private fun writeLinks(
        writer: BufferedWriter,
        post: Post,
    ) {
        if (post.links.isEmpty()) return

        writer.write("<ul class=\"links\">")
        writer.newLine()
        for (link in post.links) {
            val safeLink = escapeHtml(link)
            writer.write("  <li><a href=\"$safeLink\">$safeLink</a></li>")
            writer.newLine()
        }
        writer.write("</ul>")
        writer.newLine()
        writer.newLine()
    }

    private fun writePhotos(
        writer: BufferedWriter,
        post: Post,
        exportDir: Path,
        options: ConvertOptions,
    ) {
        for (photo in post.photos) {
            val source = exportDir.resolve(photo)
            val relative = Paths.get(
                "your_facebook_activity",
                "posts",
                "media"
            ).relativize(photo)

            val destination = options.outputDir
                .resolve(options.imageRoot)
                .resolve(relative)

            Files.createDirectories(destination.parent)

            if (Files.exists(source)) {
                Files.copy(
                    source,
                    destination,
                    StandardCopyOption.REPLACE_EXISTING
                )

                val imagePath = Paths.get("..")
                    .resolve(options.imageRoot)
                    .resolve(relative)

                val src = imagePath.toString().replace('\\', '/')
                writer.write("<img src=\"$src\" alt=\"Post Image\">")
                writer.newLine()
                writer.newLine()
            }
        }
    }

    private fun writeVideos(
        writer: BufferedWriter,
        post: Post,
        exportDir: Path,
        options: ConvertOptions,
    ) {
        for (video in post.videos) {
            val source = exportDir.resolve(video)
            val relative = Path.of(
                "your_facebook_activity",
                "posts",
                "media"
            ).relativize(video)

            val destination = options.outputDir
                .resolve(options.videoRoot)
                .resolve(relative)

            Files.createDirectories(destination.parent)

            if (Files.exists(source)) {
                Files.copy(
                    source,
                    destination,
                    StandardCopyOption.REPLACE_EXISTING
                )

                val videoPath = Path.of("..")
                    .resolve(options.videoRoot)
                    .resolve(relative)

                val src = videoPath.toString().replace('\\', '/')
                writer.write("<video controls preload=\"metadata\">")
                writer.newLine()
                writer.write("    <source src=\"$src\" type=\"video/mp4\">")
                writer.newLine()
                writer.write("</video>")
                writer.newLine()
                writer.newLine()
            }
        }
    }

    private fun createOutputFile(
        post: Post,
        options: ConvertOptions,
    ): Path {
        val contentDir = options.outputDir.resolve(options.contentDir)
        Files.createDirectories(contentDir)

        val baseName = fileFormatter.format(post.timestamp)
        var outputFile = contentDir.resolve("$baseName.html")
        var counter = 2

        while (Files.exists(outputFile)) {
            outputFile = contentDir.resolve("$baseName-$counter.html")
            counter++
        }

        return outputFile
    }

    private fun escapeHtml(text: String): String {
        return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;")
    }
}

