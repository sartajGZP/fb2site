package `in`.sartaj.fb2site.util

import org.nibor.autolink.LinkExtractor
import org.nibor.autolink.LinkType
import java.util.EnumSet

private val linkExtractor = LinkExtractor.builder()
    .linkTypes(EnumSet.of(LinkType.URL))
    .build()

// Use this in MarkdownWriter.kt
fun linkifyMarkdown(text: String): String {
    if (text.isEmpty()) return text

    val result = StringBuilder()
    var last = 0

    for (link in linkExtractor.extractLinks(text)) {
        result.append(text.substring(last, link.beginIndex))
        
        val url = text.substring(link.beginIndex, link.endIndex)
        result.append("<").append(url).append(">")
        
        last = link.endIndex
    }

    result.append(text.substring(last))
    return result.toString()
}

// Use this in HtmlWriter.kt
fun linkifyHtml(text: String): String {
    if (text.isEmpty()) return text

    val result = StringBuilder()
    var last = 0

    for (link in linkExtractor.extractLinks(text)) {
        result.append(text.substring(last, link.beginIndex))
        
        val url = text.substring(link.beginIndex, link.endIndex)
        result.append("""<a href="$url" target="_blank" rel="noopener noreferrer">$url</a>""")
        
        last = link.endIndex
    }

    result.append(text.substring(last))
    return result.toString()
}

