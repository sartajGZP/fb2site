package `in`.sartaj.fb2site.writer.html

import `in`.sartaj.fb2site.model.ConvertOptions
import `in`.sartaj.fb2site.model.NormalisedPost
import java.io.BufferedWriter
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

internal object FrontMatterWriter {

    private val yearFormatter = DateTimeFormatter.ofPattern("yyyy").withZone(ZoneOffset.UTC)
    private val monthFormatter = DateTimeFormatter.ofPattern("MM").withZone(ZoneOffset.UTC)

    fun write(
        writer: BufferedWriter,
        post: NormalisedPost,
        options: ConvertOptions,
    ) {
        writer.write("---")
        writer.newLine()
        writer.write("""title: "${post.title.replace("\"", "\\\"")}"""")
        writer.newLine()
        writer.write("date: ${post.timestamp}")
        writer.newLine()
        writer.write("""year: "${yearFormatter.format(post.timestamp)}"""")
        writer.newLine()
        writer.write("""month: "${monthFormatter.format(post.timestamp)}"""")
        writer.newLine()

        if (post.id != null) {
            writer.write("""id: "${post.id}"""")
            writer.newLine()
        }
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

        val lang = if (post.language.isBlank() || post.language == "unknown") "en" else post.language
        writer.write("layout: ${options.layout}")
        writer.newLine()
        writer.write("lang: $lang")
        writer.newLine()
        writer.write("tags:")
        writer.newLine()
        writer.write("  - facebook")
        writer.newLine()
        writer.write("---")
        writer.newLine()
        writer.newLine()
    }
}
