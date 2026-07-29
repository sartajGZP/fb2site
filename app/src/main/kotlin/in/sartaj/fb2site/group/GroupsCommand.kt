package `in`.sartaj.fb2site.groups

import `in`.sartaj.fb2site.writer.MarkdownWriter
import `in`.sartaj.fb2site.model.ConvertOptions

import java.nio.file.Paths

fun groupsCommand(args: Array<String>) {

    if (args.size != 3) {
        println("Usage: fb2site groups <group.json> <output>")
        return
    }

    val options = ConvertOptions(
        outputDir = Paths.get(args[2])
    )

    val posts = parseGroupPosts(
        Paths.get(args[1])
    )

    for (post in posts) {
        MarkdownWriter.writePost(
            post,
            Paths.get(args[1]).parent,
            options
        )
    }

    println("Converted ${posts.size} group posts.")
}
