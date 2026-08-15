package `in`.sartaj.fb2site.model

import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.nio.file.Path

data class Post(
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
    val group: String? = null
) {
    // Standard ISO-8601 string: e.g. "2026-05-04T03:31:03Z"
    val isoDate: String
        get() = DateTimeFormatter.ISO_INSTANT.format(timestamp)

    // Derived UTC year: e.g. "2026"
    val year: String
        get() = DateTimeFormatter.ofPattern("yyyy")
            .withZone(ZoneOffset.UTC)
            .format(timestamp)

    // Derived UTC month: e.g. "05"
    val month: String
        get() = DateTimeFormatter.ofPattern("MM")
            .withZone(ZoneOffset.UTC)
            .format(timestamp)
}

