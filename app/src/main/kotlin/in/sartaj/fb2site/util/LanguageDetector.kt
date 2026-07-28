package `in`.sartaj.fb2site.language

object LanguageDetector {

    fun detect(text: String): String {
        if (text.isBlank()) {
            return "unknown"
        }

        return if (text.any { it in '\u0900'..'\u097F' }) {
            "hi"
        } else {
            "en"
        }
    }
}
