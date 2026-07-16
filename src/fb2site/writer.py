from pathlib import Path

from .models import Post


def write_post(post: Post, output_dir: Path) -> None:
    output_dir.mkdir(parents=True, exist_ok=True)

    filename = post.timestamp.strftime("%Y-%m-%d-%H%M%S") + ".md"

    path = output_dir / filename

    with path.open("w", encoding="utf-8") as f:
        f.write("---\n")
        f.write(f"title: {post.title}\n")
        f.write(f"date: {post.timestamp.isoformat()}\n")
        f.write("tags:\n")
        f.write("  - facebook\n")
        f.write("---\n\n")

        if post.body:
            f.write(post.body + "\n\n")

        for link in post.links:
            f.write(f"{link}\n")
