package `in`.sartaj.fb2site.takeout
import `in`.sartaj.fb2site.model.Post
import `in`.sartaj.fb2site.util.fixMojibake

import java.time.Instant
import java.nio.file.Files
import java.nio.file.Path
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.fasterxml.jackson.databind.JsonNode

private val mapper = ObjectMapper().registerKotlinModule()

fun findPostsFiles(root: Path): List<Path> {
    val postsDir = root
        .resolve("your_facebook_activity")
        .resolve("posts")

    if (!Files.exists(postsDir)) {
        return emptyList()
    }

    return Files.list(postsDir)
        .filter {
            it.fileName.toString()
                .startsWith("your_posts__check_ins__photos_and_videos")
        }
        .sorted()
        .toList()
}

fun readPostsFile(file: Path): JsonNode {
    return mapper.readTree(Files.readString(file))
}

fun extractBody(raw: JsonNode): String {

    raw.path("data").forEach { item ->
        val text = item.path("post").asText()

        if (text.isNotBlank()) {
            return text
        }
    }

    raw.path("attachments").forEach { attachment ->

        attachment.path("data").forEach { item ->

            val text = item.path("post").asText()

            if (text.isNotBlank()) {
                return text
            }
        }
    }

    return ""
}

fun extractLinks(raw: JsonNode): List<String> {

    val links = mutableListOf<String>()

    raw.path("attachments").forEach { attachment ->

        attachment.path("data").forEach { item ->

            val url = item
                .path("external_context")
                .path("url")
                .asText()

            if (url.isNotBlank()) {
                links.add(url)
            }
        }
    }

    return links
}

fun extractPhotos(raw: JsonNode): List<Path> {

    val photos = mutableListOf<Path>()

    raw.path("attachments").forEach { attachment ->

        attachment.path("data").forEach { item ->

            val uri = item
                .path("media")
                .path("uri")
                .asText()

            if (uri.isNotBlank()) {
                photos.add(Path.of(uri))
            }
        }
    }

    return photos
}

fun extractVideos(raw: JsonNode): List<Path> {

    val videos = mutableListOf<Path>()

    val uri = raw.path("uri").asText()

    if (uri.isNotBlank()) {
        videos.add(Path.of(uri))
    }

    return videos
}

fun parsePost(raw: JsonNode): Post {

    val timestamp = Instant.ofEpochSecond(
        raw.path("timestamp").asLong()
    )

    val title = fixMojibake(
        raw.path("title").asText("")
    )

    val body = fixMojibake(
        extractBody(raw)
    )

    return Post(
        id = null,
        timestamp = timestamp,
        title = title,
        body = body,
	links = extractLinks(raw),
	photos = extractPhotos(raw),
	videos = extractVideos(raw),
        language = "unknown"
    )
}
