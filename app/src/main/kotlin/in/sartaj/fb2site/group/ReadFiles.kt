package `in`.sartaj.fb2site.groups

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.nio.file.Path

private val mapper =
    ObjectMapper().registerKotlinModule()

fun readGroupPostsFile(
    file: Path,
): List<JsonNode> {

val root = mapper.readTree(file.toFile())
    return root.toList()
}
