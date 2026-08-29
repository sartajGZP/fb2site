package `in`.sartaj.fb2site.writer.html

import `in`.sartaj.fb2site.model.NormalisedPost
import java.io.BufferedWriter

internal object LinksWriter {

    fun write(
        writer: BufferedWriter,
        post: NormalisedPost,
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
}
