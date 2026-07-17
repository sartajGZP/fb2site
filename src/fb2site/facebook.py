import json
from pathlib import Path
from datetime import datetime
import pprint

from .models import Post
from .language import detect_language

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


def extract_photos(raw: dict) -> list[Path]:
    photos = []

    for attachment in raw.get("attachments", []):
        for item in attachment.get("data", []):
            media = item.get("media")

            if not media:
                continue

            uri = media.get("uri")
            if uri:
                photos.append(Path(uri))

    return photos

def extract_videos(raw: dict) -> list[Path]:
    videos = []

    uri = raw.get("uri")
    if uri:
        videos.append(Path(uri))

    return videos

def parse_post(raw: dict) -> Post:
    """Convert a raw Facebook JSON object into a Post."""

    timestamp = datetime.fromtimestamp(raw.get("timestamp", 0))
    title = fix_mojibake(raw.get("title", ""))
    body = fix_mojibake(extract_body(raw))

    language = detect_language(title + "\n" + body)

    return Post(
        id=None,
        timestamp=timestamp,
        title=title,
        body=body,
        links=extract_links(raw),
        photos=extract_photos(raw),
        videos=extract_videos(raw),
        language=language,
    )

def parse_posts(export_dir: Path) -> list[Post]:
    posts_dir = (
        export_dir
        / "your_facebook_activity"
        / "posts"
    )

    if not posts_dir.exists():
        return []

    posts = []

    for post_file in sorted(
        posts_dir.glob("your_posts__check_ins__photos_and_videos*.json")
    ):
        with post_file.open(encoding="utf-8") as f:
            raw_posts = json.load(f)

        for raw in raw_posts:
            posts.append(parse_post(raw))

    return posts

def parse_video(raw: dict) -> Post:
    timestamp = datetime.fromtimestamp(raw.get("creation_timestamp", 0))
    title = fix_mojibake(raw.get("title", ""))
    body = fix_mojibake(raw.get("description", ""))

    language = detect_language(title + "\n" + body)

    return Post(
        id=None,
        timestamp=timestamp,
        title=title or "Video",
        body=body,
        links=[],
        photos=[],
        videos=extract_videos(raw),
        language=language,
    )

def parse_videos(export_dir: Path) -> list[Post]:
    video_file = (
    export_dir
    / "your_facebook_activity"
    / "posts"
    / "your_videos.json"
    )

    if not video_file.exists():
        return []

    with video_file.open(encoding="utf-8") as f:
        raw = json.load(f)

    videos = []

    for item in raw.get("videos_v2", []):
        videos.append(parse_video(item))

    return videos

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
