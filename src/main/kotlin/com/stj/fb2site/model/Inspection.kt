package com.stj.fb2site.model

data class Inspection(
    var inputExists: Boolean = false,
    var isFacebookExport: Boolean = false,
    var exportType: String? = null,
    var version: String? = null,

    var stats: ExportStats = ExportStats(),

    val warnings: MutableList<String> = mutableListOf(),
    val errors: MutableList<String> = mutableListOf()
)
