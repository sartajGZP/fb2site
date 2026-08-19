package `in`.sartaj.fb2site.cli

import `in`.sartaj.fb2site.groups.parseGroupPosts
import `in`.sartaj.fb2site.inspect.inspectExport
import `in`.sartaj.fb2site.model.ConvertOptions
import `in`.sartaj.fb2site.model.OutputFormat
import `in`.sartaj.fb2site.model.NormalisedPost
import `in`.sartaj.fb2site.takeout.TakeoutArchive
import `in`.sartaj.fb2site.takeout.parsePosts
import `in`.sartaj.fb2site.takeout.parseVideos
import `in`.sartaj.fb2site.writer.HtmlWriter
import `in`.sartaj.fb2site.writer.MarkdownWriter
import `in`.sartaj.fb2site.writer.TextWriter
import java.nio.file.Path
import java.nio.file.Paths

private fun writePostByFormat(
    post: NormalisedPost,
    exportDir: Path,
    options: ConvertOptions
) {
	// see model / Convert options for formatting 
    when (options.format) {
        OutputFormat.HTML -> {
            HtmlWriter.writePost(post, exportDir, options)
        }
        OutputFormat.MARKDOWN -> {
            MarkdownWriter.writePost(post, exportDir, options)
        }
        OutputFormat.TEXT -> {
            TextWriter.writePost(post, options)
        }
        OutputFormat.ALL -> {
            HtmlWriter.writePost(post, exportDir, options)
            TextWriter.writePost(post, options)
        }
    }
}

fun convertCommand(args: Array<String>) {
    if (args.size < 3) {
        println("Usage: fb2site convert <takeout.zip> <output> [--html|--markdown|--text]")
        return
    }

    val format = parseFormatFlag(args)
    val archive = TakeoutArchive(Paths.get(args[1]))
    val options = ConvertOptions(
        outputDir = Paths.get(args[2]),
        format = format
    )

    val allPosts = parsePosts(archive.path) + parseVideos(archive.path)

    for (post in allPosts) {
        writePostByFormat(post, archive.path, options)
    }

    println("Converted ${allPosts.size} posts in $format mode.")
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
          fb2site convert <takeout.zip> <output> [--html|--markdown|--text]
          fb2site groups <group.json> <output> [--html|--markdown|--text]
          fb2site version
          fb2site help
        """.trimIndent()
    )
}

fun groupsCommand(args: Array<String>) {
    if (args.size < 3) {
        println("Usage: fb2site groups <group.json> <output> [--html|--markdown|--text]")
        return
    }

    val format = parseFormatFlag(args)
    val options = ConvertOptions(
        outputDir = Paths.get(args[2]),
        format = format
    )

    val inputFile = Paths.get(args[1])
    val posts = parseGroupPosts(inputFile)
    val parentDir = inputFile.toAbsolutePath().parent ?: Paths.get(".")

    for (post in posts) {
        writePostByFormat(post, parentDir, options)
    }

    println("Converted ${posts.size} group posts in $format mode.")
}

private fun parseFormatFlag(args: Array<String>): OutputFormat {
    return args.map { it.lowercase() }.firstNotNullOfOrNull { flag ->
        when (flag) {
            "--html" -> OutputFormat.HTML
            "--markdown", "--md" -> OutputFormat.MARKDOWN
            "--text", "--txt" -> OutputFormat.TEXT
            else -> null
        }
    } ?: OutputFormat.ALL
}

