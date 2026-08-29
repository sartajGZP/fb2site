package `in`.sartaj.fb2site.writer.html

import `in`.sartaj.fb2site.model.NormalisedPost
import `in`.sartaj.fb2site.util.linkifyHtml
import java.io.BufferedWriter

internal object BodyWriter {

    fun write(
        writer: BufferedWriter,
        post: NormalisedPost,
    ) {
        if (post.body.isNotBlank()) {
            post.body.trim().split(Regex("\r?\n\r?\n+")).forEach { paragraph ->
                val safeParagraph = escapeHtml(paragraph).replace("\n", "<br>")
                val linkifiedParagraph = linkifyHtml(safeParagraph)
                writer.write("<p>$linkifiedParagraph</p>")
                writer.newLine()
            }
            writer.newLine()
        }

        post.sharedPost?.let { shared ->
            writer.write("<blockquote>")
            writer.newLine()
            if (shared.author != null) {
                writer.write("  <strong>${escapeHtml(shared.author)}:</strong><br>")
                writer.newLine()
            }
            if (shared.body.isNotBlank()) {
                val safeBody = escapeHtml(shared.body).replace("\n", "<br>")
                writer.write("  <p>${linkifyHtml(safeBody)}</p>")
                writer.newLine()
            }
            writer.write("</blockquote>")
            writer.newLine()
            writer.newLine()
        }
    }
}
