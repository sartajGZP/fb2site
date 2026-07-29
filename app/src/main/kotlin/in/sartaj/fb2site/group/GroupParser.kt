package `in`.sartaj.fb2site.groups

import com.fasterxml.jackson.databind.JsonNode
import `in`.sartaj.fb2site.model.Post
import `in`.sartaj.fb2site.util.detectLanguage
import `in`.sartaj.fb2site.util.fixMojibake
import `in`.sartaj.fb2site.util.linkify
import java.nio.file.Path
import java.time.Instant


fun parseGroupPost(raw: JsonNode): Post {

    val body = linkify(
        fixMojibake(
            raw.path("text").asText("")
        )
    )

    return Post(
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
): List<Post> {

    val json =
        readGroupPostsFile(file)

    return json.map(::parseGroupPost)
}
