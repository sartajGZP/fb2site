package `in`.sartaj.fb2site.parser

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import `in`.sartaj.fb2site.model.NormalisedPost
import `in`.sartaj.fb2site.model.SharedPost
import `in`.sartaj.fb2site.util.detectLanguage
import `in`.sartaj.fb2site.util.fixMojibake
import `in`.sartaj.fb2site.util.linkifyMarkdown
import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant

fun parseUserPosts(file: Path): List<NormalisedPost> {
    val mapper = ObjectMapper()

    Files.newBufferedReader(file).use { reader ->
        val root = mapper.readTree(reader)

        return root
            .filter { it.path("type").asText("") == "post" }
            .map(::parseUserPost)
    }
}

private fun parseUserPost(raw: JsonNode): NormalisedPost {
    val body = linkifyMarkdown(
        fixMojibake(
            raw.path("message").asText("")
        )
    )

    val sharedPost =
        raw.path("attached_post")
            .takeUnless { it.isMissingNode || it.isNull }
            ?.let(::parseSharedPost)

    return NormalisedPost(
        id = raw.path("post_id").asText(null),

        timestamp = Instant.ofEpochSecond(
            raw.path("timestamp").asLong()
        ),

        title = "",

        body = body,

        links = emptyList(),

        photos = emptyList(),

        videos = emptyList(),

        language = detectLanguage(body),

        author = raw.path("author")
            .path("name")
            .asText(null),

        facebookUrl = raw.path("url")
            .asText(null),

        group = raw.path("associated_group")
            .path("name")
            .asText(null),

        sharedPost = sharedPost
    )
}

private fun parseSharedPost(
    raw: JsonNode
): SharedPost {

    val body = linkifyMarkdown(
        fixMojibake(
            raw.path("message").asText("")
        )
    )

    val timestamp =
        raw.path("timestamp")
            .takeUnless { it.isMissingNode || it.isNull }
            ?.asLong()
            ?.let(Instant::ofEpochSecond)

    return SharedPost(
        id = raw.path("post_id").asText(null),

        timestamp = timestamp,

        body = body,

        author = raw.path("author")
            .path("name")
            .asText(null),

        facebookUrl = raw.path("url")
            .asText(null),

        photos = emptyList(),

        videos = emptyList()
    )
}
