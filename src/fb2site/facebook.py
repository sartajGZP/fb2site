import json
from pathlib import Path
from datetime import datetime
import pprint

from .models import Post

def extract_body(raw: dict) -> str:
    """Extract the text body of a Facebook post."""

    for item in raw.get("data", []):
        post = item.get("post")
        if post:
            return post

    return ""


def parse_post(raw: dict) -> Post:
    """Convert a raw Facebook JSON object into a Post."""

    timestamp = datetime.fromtimestamp(raw.get("timestamp", 0))
    title = raw.get("title", "")

    return Post(
        id=None,
        timestamp=timestamp,
        title=title,
        body=extract_body(raw),
    )

def parse_posts(export_dir: Path) -> list[Post]:
    post_file = (
        export_dir
        / "your_facebook_activity"
        / "posts"
        / "your_posts__check_ins__photos_and_videos_1.json"
    )

    with post_file.open(encoding="utf-8") as f:
        raw_posts = json.load(f)

    posts = []

    for raw in raw_posts:
        posts.append(parse_post(raw))

    return posts
