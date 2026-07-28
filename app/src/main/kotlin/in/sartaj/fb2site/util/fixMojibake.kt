package `in`.sartaj.fb2site.util

import java.nio.charset.StandardCharsets


fun fixMojibake(text: String): String {
    if (!text.contains("à")) {
        return text
    }

    return try {
        String(
            text.toByteArray(StandardCharsets.ISO_8859_1),
            StandardCharsets.UTF_8
        )
    } catch (e: Exception) {
        text
    }
}
