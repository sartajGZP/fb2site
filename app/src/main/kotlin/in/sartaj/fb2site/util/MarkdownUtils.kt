package `in`.sartaj.fb2site.util

fun escapeMarkdown(text: String): String =
    text
        .replace("\\", "\\\\")
        .replace("#", "\\#")
        .replace("*", "\\*")
        .replace("_", "\\_")
        .replace(">", "\\>")
        .replace("`", "\\`")
