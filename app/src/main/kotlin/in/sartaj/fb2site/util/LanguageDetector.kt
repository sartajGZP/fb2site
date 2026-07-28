package `in`.sartaj.fb2site.util

    fun detectLanguage(text: String): String {
var devanagari = 0
var latin = 0

for (c in text) {
    when {
        c in '\u0900'..'\u097F' -> devanagari++
        c.isLetter() && c.code < 128 -> latin++
    }
}

return when {
    devanagari == 0 && latin == 0 -> "unknown"
    devanagari > latin -> "hi"
    else -> "en"
}
}
