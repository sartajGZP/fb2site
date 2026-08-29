package `in`.sartaj.fb2site.writer.html

import `in`.sartaj.fb2site.model.ConvertOptions
import `in`.sartaj.fb2site.model.NormalisedPost

import java.io.BufferedWriter
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object FrontMatter {

    private val yearFormatter =
        DateTimeFormatter.ofPattern("yyyy")
            .withZone(ZoneOffset.UTC)

    private val monthFormatter =
        DateTimeFormatter.ofPattern("MM")
            .withZone(ZoneOffset.UTC)

    private val monthNameFormatter =
        DateTimeFormatter.ofPattern("MMMM")
            .withZone(ZoneOffset.UTC)

    private val dayFormatter =
        DateTimeFormatter.ofPattern("dd")
            .withZone(ZoneOffset.UTC)

    fun write(
        writer: BufferedWriter,
        post: NormalisedPost,
        options: ConvertOptions,
    ) {
        val year = yearFormatter.format(post.timestamp)
        val month = monthFormatter.format(post.timestamp)
        val day = dayFormatter.format(post.timestamp)

        writer.write("---")
        writer.newLine()

        writer.write("""title: "${escapeYaml(post.title)}"""")
        writer.newLine()

        writer.write("date: ${post.timestamp}")
        writer.newLine()

        writer.write("""year: "$year"""")
        writer.newLine()

        writer.write("""month: "$month"""")
        writer.newLine()

        if (post.id != null) {
            writer.write("""id: "${escapeYaml(post.id)}"""")
            writer.newLine()
        }

        if (post.author != null) {
            writer.write("""author: "${escapeYaml(post.author)}"""")
            writer.newLine()
        }

        if (post.facebookUrl != null) {
            writer.write("""facebook: "${escapeYaml(post.facebookUrl)}"""")
            writer.newLine()
        }

        if (post.group != null) {
            writer.write("""group: "${escapeYaml(post.group)}"""")
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

        // Eleventy Navigation
        writer.write("eleventyNavigation:")
        writer.newLine()
        writer.write("""  key: "$year-$month-$day"""")
        writer.newLine()
        writer.write("""  title: "$day"""")
        writer.newLine()
        writer.write("""  parent: "$year-$month"""")
        writer.newLine()

        writer.write("---")
        writer.newLine()
        writer.newLine()
    }

    private fun escapeYaml(value: String): String {
        return value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
    }
}
