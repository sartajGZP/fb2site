from .facebook import parse_posts, parse_videos
from .writer import write_post


def convert_export(export_dir, output_dir):
    posts = parse_posts(export_dir)
    posts.extend(parse_videos(export_dir))

    for post in posts:
        write_post(post, export_dir, output_dir)

    print(f"Converted {len(posts)} posts.")
