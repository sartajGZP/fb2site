package `in`.sartaj.fb2site.model

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Defines the output file format options for exported Facebook content.
 */
enum class OutputFormat {
    ALL,
    HTML,      
    MARKDOWN,  
    TEXT      
    // only texts are generated without front matter
}

/**
 * Configuration data holder for the Facebook-to-static-site conversion pipeline.
 *
 * ### Why This Class Exists:
 * 1. **Centralized Configuration**: Prevents "long parameter lists" in writer and parser functions.
 *    Instead of passing `outputDir`, `imageRoot`, `videoRoot`, `layout`, etc., individually down the call chain,
 *    a single `ConvertOptions` instance is passed down to all writers (`HtmlWriter`, `TextWriter`, `MarkdownWriter`).
 * 2. **Sensible Defaults**: Pre-configures standard Eleventy project directory conventions (like `fb-export`,
 *    `assets/img/fb`, and `layouts/fb.njk`), allowing commands to be run with minimal required CLI input.
 * 3. **Flexibility & Testability**: Decouples output path resolution logic from execution commands,
 *    making it easy to swap destination directories or target output formats (e.g., `--html`, `--markdown`).
 */
data class ConvertOptions(
    /** Absolute or relative path where generated files and asset directories will be written. */
    val outputDir: Path,

    /** Subdirectory under [outputDir] where HTML/Markdown posts will be placed (e.g., `_site_src/fb-export`). */
    val contentDir: Path = Paths.get("fb-export"),

    /** Relative path from site root where exported post photos will be copied. */
    val imageRoot: Path = Paths.get("assets/img/fb"),

    /** Relative path from site root where exported post videos will be copied. */
    val videoRoot: Path = Paths.get("assets/video/fb"),

    /** Default Eleventy layout template used in the front matter header of generated posts. */
    val layout: String = "layouts/fb.njk",

    /** Target file format mode (defaults to ALL). Controlled via CLI flags like `--html` or `--markdown`. */
    val format: OutputFormat = OutputFormat.ALL
)

