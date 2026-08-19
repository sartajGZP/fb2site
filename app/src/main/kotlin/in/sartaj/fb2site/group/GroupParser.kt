package `in`.sartaj.fb2site.groups

import com.fasterxml.jackson.databind.JsonNode
import `in`.sartaj.fb2site.model.NormalisedPost
import `in`.sartaj.fb2site.util.detectLanguage
import `in`.sartaj.fb2site.util.fixMojibake
import `in`.sartaj.fb2site.util.linkifyMarkdown
import java.nio.file.Path
import java.time.Instant


fun parseGroupPost(raw: JsonNode): NormalisedPost {
    val body = fixMojibake(
        raw.path("text").asText("")
	)

    return NormalisedPost(
        id = raw.path("id").asText(),
        timestamp = Instant.parse(
            raw.path("time").asText()
        ),
        title = "",
        body = body,
        links = emptyList(),
        photos = emptyList(),
        videos = emptyList(),
        language = detectLanguage(body),
        author = raw.path("user")
            .path("name")
            .asText(null),
        facebookUrl = raw.path("url")
            .asText(null),
        group = raw.path("groupTitle")
            .asText(null)
    )
}

fun parseGroupPosts(
    file: Path,
): List<NormalisedPost> {

    val json =
        readGroupPostsFile(file)

    return json.map(::parseGroupPost)
}
