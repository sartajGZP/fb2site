import json
from datetime import datetime
from pathlib import Path

from .models import Post
from .language import detect_language

def parse_posts(json_file: Path) -> list[Post]:
    with json_file.open(encoding="utf-8") as f:
        data = json.load(f)

    print(f"Loaded {len(data)} posts.")

    posts = []

    for raw in data:
        body = raw.get("text", "")

        post = Post(
            id=str(raw.get("legacyId")),
            timestamp=datetime.fromisoformat(raw["time"].replace("Z", "+00:00")),
            title="",
            body=body,
            author=raw.get("user", {}).get("name"),
            facebook_url=raw.get("url"),
            group=raw.get("groupTitle"),
            language=detect_language(body),
        )

        posts.append(post)

    return posts
