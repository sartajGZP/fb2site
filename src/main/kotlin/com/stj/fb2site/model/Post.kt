package com.stj.fb2site.model

import java.time.Instant

data class Post(
    val id: String?,
    val timestamp: Instant,
    val title: String,
    val body: String,

    val links: List<String> = emptyList(),
    val photos: List<String> = emptyList(),
    val videos: List<String> = emptyList(),

    val language: String = "unknown",
    val author: String? = null,
    val facebookUrl: String? = null,
    val group: String? = null
)
