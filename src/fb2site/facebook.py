import json
from pathlib import Path
from datetime import datetime
import pprint

from .models import Post

def fix_mojibake(text: str) -> str:
    if "à" not in text:
        return text

    try:
        return text.encode("latin1").decode("utf-8")
    except (UnicodeEncodeError, UnicodeDecodeError):
        return text

def extract_body(raw: dict) -> str:
    """Extract the main text body of a Facebook post."""

    # First, look in the post's data.
    for item in raw.get("data", []):
        text = item.get("post")
        if text:
            return text

    # Then, look inside attachments.
    for attachment in raw.get("attachments", []):
        for item in attachment.get("data", []):
            text = item.get("post")
            if text:
                return text

    return ""

def parse_post(raw: dict) -> Post:
    """Convert a raw Facebook JSON object into a Post."""

    timestamp = datetime.fromtimestamp(raw.get("timestamp", 0))
    title = fix_mojibake(raw.get("title", ""))

    return Post(
        id=None,
        timestamp=timestamp,
        title=title,
        body = fix_mojibake(extract_body(raw)),
        links=extract_links(raw),
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
        post = parse_post(raw)

        if "shared a post" in post.title and post.body:
            print("=" * 60)
            print(post.title)
            print(post.body[:100])
            print("=" * 60)

        posts.append(post)
    return posts


def extract_links(raw: dict) -> list[str]:
    links = []

    for attachment in raw.get("attachments", []):
        for item in attachment.get("data", []):
            external = item.get("external_context")

            if not external:
                continue

            url = external.get("url")

            if url:
                links.append(url)

    return links
