package `in`.sartaj.fb2site.inspect

import `in`.sartaj.fb2site.model.Inspection
import java.nio.file.Files
import java.nio.file.Path

private val EXPECTED_DIRS = listOf(
    "your_facebook_activity",
    "personal_information"
)

fun inspectExport(path: Path): Inspection {
    val result = Inspection()

    if (!Files.exists(path)) {
        result.errors.add("$path does not exist")
        return result
    }

    if (!Files.isDirectory(path)) {
        result.errors.add("$path is not a directory")
        return result
    }

    result.inputExists = true

    val missing = EXPECTED_DIRS.filter {
        !Files.exists(path.resolve(it))
    }

    if (missing.isNotEmpty()) {
        result.errors.addAll(
            missing.map { "Missing directory: $it" }
        )
        return result
    }

    result.isFacebookExport = true
    result.exportType = "facebook"

    return result
}
