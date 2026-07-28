package in.sartaj.fb2site.util

import org.nibor.autolink.LinkExtractor
import org.nibor.autolink.LinkType

private val linkExtractor = LinkExtractor.builder()
    .linkTypes(EnumSet.of(LinkType.URL))
    .build()
"""
fun linkify(text: String): String {
    if (text.isEmpty()) {
        return text
    }

    val result = StringBuilder()
    var last = 0

    for (link in linkExtractor.extractLinks(text)) {
        result.append(text.substring(last, link.beginIndex))

        val url = text.substring(link.beginIndex, link.endIndex)
        result.append("<")
        result.append(url)
        result.append(">")

        last = link.endIndex
    }

    result.append(text.substring(last))

    return result.toString()
}
"""
fun linkify(text: String): String = text
