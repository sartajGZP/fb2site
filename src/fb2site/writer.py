from pathlib import Path
from shutil import copy2

from .models import Post
from .options import ConvertOptions
from .markdown import escape_markdown
from .linkify import linkify

def write_post(
    post: Post,
    export_dir: Path,
    options: ConvertOptions,
) -> None:
    output_dir = options.output_dir

    filename = post.timestamp.strftime("%Y-%m-%d-%H%M%S") + ".md"

    content_dir = output_dir / options.content_dir
    content_dir.mkdir(parents=True, exist_ok=True)

    path = content_dir / filename

    with path.open("w", encoding="utf-8") as f:
        # Front matter
        f.write("---\n")
        #title = escape_markdown(post.title).replace('"', '\\"')
        #title = post.title.replace('"', '\\"')
        f.write(f'title: "{post.title}"\n')
        f.write(f"date: {post.timestamp.isoformat()}\n")
        if post.author:
            #f.write(f'author: "{post.author}"\n')
            pass

        f.write(f'author: "Sartaj Ansari"\n')

        if post.facebook_url:
            f.write(f'facebook: "{post.facebook_url}"\n')

        if post.group:
            f.write(f'group: "{post.group}"\n')
        f.write(f"layout: {options.layout}\n")
        f.write(f"lang: {post.language}\n")
        f.write("tags:\n")
        f.write("  - facebook\n")
        f.write("---\n\n")

        # Body
        if post.body:
            f.write(linkify(escape_markdown(post.body)))
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

            destination = output_dir / options.image_root / relative
            destination.parent.mkdir(parents=True, exist_ok=True)

            if source.exists():
                copy2(source, destination)

                image_path = Path("..") / options.image_root / relative
                f.write(f"![]({image_path.as_posix()})\n\n")

        # Videos
        for video in post.videos:
            source = export_dir / video

            relative = video.relative_to(
                "your_facebook_activity/posts/media"
            )

            destination = output_dir / options.video_root / relative
            destination.parent.mkdir(parents=True, exist_ok=True)

            if source.exists():
                copy2(source, destination)

                video_path = Path("..") / options.video_root / relative

                f.write("<video controls preload=\"metadata\">\n")
                f.write(
                    f'  <source src="{video_path.as_posix()}" type="video/mp4">\n'
                )
                f.write("</video>\n\n")
