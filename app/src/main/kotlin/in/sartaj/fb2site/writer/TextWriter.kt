package `in`.sartaj.fb2site.writer

import `in`.sartaj.fb2site.model.ConvertOptions
import `in`.sartaj.fb2site.model.Post
import java.nio.file.Files
import java.security.MessageDigest

object TextWriter {

    fun writePost(
        post: Post,
        options: ConvertOptions,
    ) {
        val body = post.body.trim()

        if (body.isBlank()) {
            return
        }

        val dir = options.outputDir
            .resolve("fb-export")
            .resolve("texts")

        Files.createDirectories(dir)

        val fileName = post.id ?: sha256(body)
        val file = dir.resolve("$fileName.txt")

        Files.writeString(file, body)
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
