package `in`.sartaj.fb2site.takeout

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.ZipInputStream
import kotlin.io.path.inputStream

class TakeoutArchive(
    zipFile: Path
) : AutoCloseable {

    val path: Path = Files.createTempDirectory("fb2site-")

    init {
        extract(zipFile)
    }

    private fun extract(zipFile: Path) {
        ZipInputStream(zipFile.inputStream()).use { zip ->

            var entry = zip.nextEntry

            while (entry != null) {

                val destination = path.resolve(entry.name)

                if (entry.isDirectory) {
                    Files.createDirectories(destination)
                } else {

                    destination.parent?.let {
                        Files.createDirectories(it)
                    }

                    Files.copy(zip, destination)
                }

                zip.closeEntry()
                entry = zip.nextEntry
            }
        }
    }

    override fun close() {
        deleteRecursively(path)
    }

    private fun deleteRecursively(path: Path) {
        if (!Files.exists(path)) {
            return
        }

        Files.walk(path)
            .sorted(Comparator.reverseOrder())
            .forEach(Files::delete)
    }
}
