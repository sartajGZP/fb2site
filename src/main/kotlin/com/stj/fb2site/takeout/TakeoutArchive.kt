package com.stj.fb2site.takeout

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.ZipInputStream
import kotlin.io.path.inputStream

class ZipExtractor(
    zipFile: Path
) : AutoCloseable {

    val root: Path = Files.createTempDirectory("fb2site-")

    init {
        extract(zipFile)
    }

    private fun extract(zipFile: Path) {
        ZipInputStream(zipFile.inputStream()).use { zip ->

            var entry = zip.nextEntry

            while (entry != null) {

                val destination = root.resolve(entry.name)

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
        deleteRecursively(root)
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
