package com.stj.fb2site.markdown

fun escapeMarkdown(text: String): String =
    text
        .replace("\\", "\\\\")
        .replace("#", "\\#")
        .replace("*", "\\*")
        .replace("_", "\\_")
        .replace(">", "\\>")
        .replace("`", "\\`")
