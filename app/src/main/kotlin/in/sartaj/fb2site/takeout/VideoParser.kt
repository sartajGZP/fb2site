package `in`.sartaj.fb2site.takeout

import `in`.sartaj.fb2site.model.NormalisedPost
import `in`.sartaj.fb2site.util.fixMojibake
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant

private val mapper = ObjectMapper().registerKotlinModule()

fun findVideosFile(root: Path): Path {

    return root
        .resolve("your_facebook_activity")
        .resolve("posts")
        .resolve("your_videos.json")
}

fun readVideosFile(file: Path): JsonNode {

    val root = mapper.readTree(Files.readString(file))

    return root["videos_v2"]
}

fun parseVideo(raw: JsonNode): NormalisedPost {

    return NormalisedPost(
        id = null,
        timestamp = Instant.ofEpochSecond(
            raw["creation_timestamp"].asLong()
        ),
        title = "Video",
        body = fixMojibake(
            raw["description"].asText()
        ),
        links = emptyList(),
        photos = emptyList(),
        videos = listOf(
            Path.of(raw["uri"].asText())
        ),
        language = "unknown",
        author = null,
        facebookUrl = null,
        group = null
    )
}


fun parseVideos(root: Path): List<NormalisedPost> {

    val videos = mutableListOf<NormalisedPost>()

    val json = readVideosFile(findVideosFile(root))

    for (node in json) {
        videos.add(parseVideo(node))
    }

    return videos
}


