package com.stj.fb2site.model

import java.nio.file.Path
import java.nio.file.Paths

data class ConvertOptions(
    val outputDir: Path,

    val contentDir: Path = Paths.get("fb-export"),

    val imageRoot: Path = Paths.get("assets/img/fb"),
    val videoRoot: Path = Paths.get("assets/video/fb"),

    val layout: String = "layouts/fb.njk"
)
