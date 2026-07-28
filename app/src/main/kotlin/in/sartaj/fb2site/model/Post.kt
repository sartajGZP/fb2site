package `in`.sartaj.fb2site.model

import java.time.Instant
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
)
