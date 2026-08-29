package `in`.sartaj.fb2site.model

import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.nio.file.Path

data class NormalisedPost(
    val id: String?,
    val timestamp: Instant,
    val title: String,
    val body: String,
    val links: List<String> = emptyList(),
    val photos: List<Path> = emptyList(),
    val videos: List<Path> = emptyList(),
    val language: String = "unknown",
    val author: String? = null,
    val facebookUrl: String? = null,
    val group: String? = null,
    val sharedPost: SharedPost? = null
) {
    val isoDate: String
        get() = DateTimeFormatter.ISO_INSTANT.format(timestamp)

    val year: String
        get() = DateTimeFormatter.ofPattern("yyyy")
            .withZone(ZoneOffset.UTC)
            .format(timestamp)

    val month: String
        get() = DateTimeFormatter.ofPattern("MM")
            .withZone(ZoneOffset.UTC)
            .format(timestamp)
}
