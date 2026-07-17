from .facebook import parse_posts, parse_videos
from .writer import write_post
from .options import ConvertOptions


def convert_export(export_dir, output_dir):
    posts = parse_posts(export_dir)
    posts.extend(parse_videos(export_dir))

    for post in posts:
        options = ConvertOptions(output_dir=output_dir)
        write_post(post, export_dir, options)
    print(f"Converted {len(posts)} posts.")
