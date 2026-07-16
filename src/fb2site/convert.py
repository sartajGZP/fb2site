from pathlib import Path

from .facebook import parse_posts
from .writer import write_post


def convert_export(export_dir: Path, output_dir: Path) -> None:
    posts = parse_posts(export_dir)

    for post in posts:
        write_post(post, output_dir)

    print(f"Converted {len(posts)} posts.")
