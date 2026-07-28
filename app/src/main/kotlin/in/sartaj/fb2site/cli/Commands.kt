package `in`.sartaj.fb2site.cli

import `in`.sartaj.fb2site.model.ConvertOptions
import `in`.sartaj.fb2site.takeout.parsePosts
import `in`.sartaj.fb2site.takeout.parseVideos
import `in`.sartaj.fb2site.writer.MarkdownWriter
import `in`.sartaj.fb2site.inspect.inspectExport
import `in`.sartaj.fb2site.takeout.TakeoutArchive
import java.nio.file.Paths

fun convertCommand(args: Array<String>) {

    if (args.size != 3) {
        println("Usage: fb2site convert <takeout.zip> <output>")
        return
    }

    val archive = TakeoutArchive(
        Paths.get(args[1])
    )

    val options = ConvertOptions(
        outputDir = Paths.get(args[2])
    )

    val allPosts =
        parsePosts(archive.path) +
        parseVideos(archive.path)

    for (post in allPosts) {
        MarkdownWriter.writePost(
            post,
            archive.path,
            options
        )
    }

    println("Converted ${allPosts.size} posts.")

    archive.close()
}

fun inspectCommand(args: Array<String>) {

    if (args.size != 2) {
        println("Usage: fb2site inspect <takeout.zip>")
        return
    }

    val archive = TakeoutArchive(Paths.get(args[1]))

    val result = inspectExport(archive.path)

    println(result)

    archive.close()
}

fun versionCommand() {
    println("fb2site 0.1.0")
}

fun printHelp() {
    println(
        """
        fb2site

        Usage:

          fb2site inspect <takeout.zip>
          fb2site convert <takeout.zip> <output>
          fb2site version
          fb2site help
        """.trimIndent()
    )
}
