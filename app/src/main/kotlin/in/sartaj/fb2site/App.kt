package `in`.sartaj.fb2site

import `in`.sartaj.fb2site.inspect.inspectExport
import `in`.sartaj.fb2site.takeout.TakeoutArchive
import `in`.sartaj.fb2site.takeout.findPostsFiles
import `in`.sartaj.fb2site.takeout.readPostsFile
import `in`.sartaj.fb2site.takeout.extractBody
import `in`.sartaj.fb2site.util.fixMojibake
import `in`.sartaj.fb2site.takeout.parsePost
import `in`.sartaj.fb2site.takeout.extractLinks
import `in`.sartaj.fb2site.writer.MarkdownWriter
import `in`.sartaj.fb2site.model.ConvertOptions
import java.nio.file.Paths

fun main(args: Array<String>) {
	println("Working directory: ${System.getProperty("user.dir")}")

    val archive = TakeoutArchive(Paths.get(args[0]))

    val result = inspectExport(archive.path)
    val files = findPostsFiles(archive.path)

    println(files)
val json  = readPostsFile(files.first())
val post = parsePost(json[6])
print(post)
println(post.photos)

MarkdownWriter.writePost(
    post,
    archive.path,
    ConvertOptions(
        outputDir = Paths.get("output")
    )
)
    println(result)


    archive.close()
}
