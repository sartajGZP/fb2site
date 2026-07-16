from pathlib import Path
from shutil import copy2

from .models import Post


def write_post(post: Post, export_dir: Path, output_dir: Path) -> None:
    output_dir.mkdir(parents=True, exist_ok=True)

    filename = post.timestamp.strftime("%Y-%m-%d-%H%M%S") + ".md"
    path = output_dir / filename

    with path.open("w", encoding="utf-8") as f:
        # Front matter
        f.write("---\n")
        f.write(f"title: {post.title}\n")
        f.write(f"date: {post.timestamp.isoformat()}\n")
        f.write("tags:\n")
        f.write("  - facebook\n")
        f.write("---\n\n")

        # Body
        if post.body:
            f.write(post.body)
            f.write("\n\n")

        # Links
        for link in post.links:
            f.write(f"<{link}>\n")

        if post.links:
            f.write("\n")

        # Photos
        for photo in post.photos:
            source = export_dir / photo

            relative = photo.relative_to(
                "your_facebook_activity/posts/media"
            )

            destination = output_dir / "images" / relative
            destination.parent.mkdir(parents=True, exist_ok=True)

            if source.exists():
                copy2(source, destination)
                f.write(f"![](images/{relative.as_posix()})\n\n")
