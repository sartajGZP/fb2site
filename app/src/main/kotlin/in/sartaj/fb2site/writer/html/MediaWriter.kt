package `in`.sartaj.fb2site.writer.html

import `in`.sartaj.fb2site.model.ConvertOptions
import `in`.sartaj.fb2site.model.NormalisedPost
import java.io.BufferedWriter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

internal object MediaWriter {

    fun writePhotos(
        writer: BufferedWriter,
        post: NormalisedPost,
        exportDir: Path,
        options: ConvertOptions,
    ) {
        for (photo in post.photos) {
            val source = exportDir.resolve(photo)
            val relative = Paths.get("your_facebook_activity", "posts", "media").relativize(photo)
            val destination = options.outputDir
                .resolve(options.imageRoot)
                .resolve(relative)

            Files.createDirectories(destination.parent)

            if (Files.exists(source)) {
                Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING)
                val imagePath = Paths.get("/").resolve(options.imageRoot).resolve(relative)
                val src = imagePath.toString().replace('\\', '/')
                writer.write("<img src=\"$src\" alt=\"NormalisedPost Image\">")
                writer.newLine()
                writer.newLine()
            }
        }
    }

    fun writeVideos(
        writer: BufferedWriter,
        post: NormalisedPost,
        exportDir: Path,
        options: ConvertOptions,
    ) {
        for (video in post.videos) {
            val source = exportDir.resolve(video)
            val relative = Path.of("your_facebook_activity", "posts", "media").relativize(video)
            val destination = options.outputDir
                .resolve(options.videoRoot)
                .resolve(relative)

            Files.createDirectories(destination.parent)

            if (Files.exists(source)) {
                Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING)
                val videoPath = Path.of("/").resolve(options.videoRoot).resolve(relative)
                val src = videoPath.toString().replace('\\', '/')
                writer.write("<video controls preload=\"metadata\">")
                writer.newLine()
                writer.write("    <source src=\"$src\" type=\"video/mp4\">")
                writer.newLine()
                writer.write("</video>")
                writer.newLine()
                writer.newLine()
            }
        }
    }
}
