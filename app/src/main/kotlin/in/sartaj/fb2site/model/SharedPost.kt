package `in`.sartaj.fb2site.model

import java.nio.file.Path
import java.time.Instant

data class SharedPost(
    val id: String? = null,
    val timestamp: Instant? = null,
    val author: String? = null,
    val facebookUrl: String? = null,
    val body: String = "",
    val links: List<String> = emptyList(),
    val photos: List<Path> = emptyList(),
    val videos: List<Path> = emptyList()
)
