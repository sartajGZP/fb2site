package `in`.sartaj.fb2site

import `in`.sartaj.fb2site.inspect.inspectExport
import `in`.sartaj.fb2site.takeout.TakeoutArchive
import `in`.sartaj.fb2site.takeout.findPostsFiles
import `in`.sartaj.fb2site.takeout.readPostsFile
import `in`.sartaj.fb2site.takeout.readVideosFile
import `in`.sartaj.fb2site.takeout.findVideosFile
import `in`.sartaj.fb2site.takeout.parsePosts
import `in`.sartaj.fb2site.takeout.parseVideos
import `in`.sartaj.fb2site.util.fixMojibake
import `in`.sartaj.fb2site.takeout.parsePost
import `in`.sartaj.fb2site.writer.MarkdownWriter
import `in`.sartaj.fb2site.writer.HtmlWriter
import `in`.sartaj.fb2site.model.ConvertOptions
import `in`.sartaj.fb2site.cli.printHelp
import `in`.sartaj.fb2site.cli.convertCommand
import `in`.sartaj.fb2site.cli.inspectCommand
import `in`.sartaj.fb2site.cli.printHelp
import `in`.sartaj.fb2site.cli.groupsCommand


import java.nio.file.Paths

fun main(args: Array<String>) {

    if (args.isEmpty()) {
        printHelp()
        return
    }

    when (args[0]) {

        "help" -> {
            printHelp()
        }

        "convert" -> {
            convertCommand(args)
        }

        "inspect" -> {
            inspectCommand(args)
        }
	"groups" -> groupsCommand(args)

        else -> {
            println("Unknown command: ${args[0]}")
            printHelp()
        }
    }
}
