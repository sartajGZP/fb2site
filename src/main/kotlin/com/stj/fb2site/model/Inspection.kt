package com.stj.fb2site.model

import java.time.Instant

data class ExportStats(
    val posts: Int = 0,
    val comments: Int = 0,
    val reactions: Int = 0,
    val photos: Int = 0,
    val videos: Int = 0,
    val albums: Int = 0
)

data class Inspection(
    val inputExists: Boolean = false,
    val isFacebookExport: Boolean = false,
    val exportType: String? = null,
    val version: String? = null,

    val stats: ExportStats = ExportStats(),

    val warnings: List<String> = emptyList(),
    val errors: List<String> = emptyList()
)

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
