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

fun parseUser2Posts(file: Path): List<NormalisedPost> {
    val mapper = ObjectMapper()

    Files.newBufferedReader(file).use { reader ->
        val root = mapper.readTree(reader)

        if (!root.isArray) {
            return emptyList()
        }

        return root
            .filter { it.isObject && it.has("postId") }
            .map(::parseUser2Post)
    }
}

private fun parseUser2Post(raw: JsonNode): NormalisedPost {

    val body = linkifyMarkdown(
        fixMojibake(
            raw.path("text").asText("")
        )
    )

    val sharedPost = raw.path("sharedPost")
        .takeUnless { it.isMissingNode || it.isNull }
        ?.let(::parseUser2SharedPost)

    val timestamp = when {
        raw.path("timestamp").canConvertToLong() ->
            Instant.ofEpochSecond(raw.path("timestamp").asLong())

        raw.path("time").isTextual ->
            Instant.parse(raw.path("time").asText())

        else ->
            Instant.EPOCH
    }

    val author = raw.path("user")
        .path("name")
        .asText(null)

    val facebookUrl = raw.path("url")
        .asText(null)
        .takeUnless { it.isNullOrBlank() }

        ?: raw.path("topLevelUrl")
            .asText(null)
            .takeUnless { it.isNullOrBlank() }

    return NormalisedPost(
        id = raw.path("postId").asText(null),

        timestamp = timestamp,

        title = "",

        body = body,

        links = extractLinks(raw),

        photos = emptyList(),

        videos = emptyList(),

        language = detectLanguage(body),

        author = author,

        facebookUrl = facebookUrl,

        group = null,

        sharedPost = sharedPost
    )
}

private fun parseUser2SharedPost(
    raw: JsonNode
): SharedPost {

    val body = linkifyMarkdown(
        fixMojibake(
            raw.path("text").asText("")
        )
    )

    val timestamp =
        when {
            raw.path("timestamp").canConvertToLong() ->
                Instant.ofEpochSecond(
                    raw.path("timestamp").asLong()
                )

            raw.path("time").isTextual ->
                Instant.parse(
                    raw.path("time").asText()
                )

            else ->
                null
        }

    val pageName = raw.path("pageName")

    val author = when {
        pageName.isObject ->
            pageName.path("name").asText(null)

        raw.path("user").isObject ->
            raw.path("user").path("name").asText(null)

        else ->
            null
    }

    val facebookUrl =
        raw.path("url")
            .asText(null)
            .takeUnless { it.isNullOrBlank() }

        ?: pageName.path("profileUrl")
            .asText(null)
            .takeUnless { it.isNullOrBlank() }

    return SharedPost(
        id = raw.path("postId").asText(null),

        timestamp = timestamp,

        body = body,

        author = author,

        facebookUrl = facebookUrl,

        photos = emptyList(),

        videos = emptyList()
    )
}

private fun extractLinks(raw: JsonNode): List<String> {
    val links = mutableListOf<String>()

    val textReferences = raw.path("textReferences")

    if (textReferences.isArray) {
        for (reference in textReferences) {
            val url = reference.path("url").asText("")

            if (url.isNotBlank()) {
                links.add(url)
            }
        }
    }

    return links.distinct()
}
