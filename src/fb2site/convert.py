from .takeout import parse_posts as parse_takeout_posts
from .group_parser import parse_posts as parse_group_posts
from .takeout import parse_videos
from .options import ConvertOptions
from .writer import write_post

def convert_export(export_dir, output_dir, format):
    if format == "takeout":
        posts = parse_takeout_posts(export_dir)
        posts.extend(parse_videos(export_dir))

    elif format == "group":
        posts = parse_group_posts(export_dir)

    else:
        raise ValueError(f"Unknown format: {format}")

    options = ConvertOptions(output_dir=output_dir)

    for post in posts:
        write_post(post, export_dir, options)

    print(f"Converted {len(posts)} posts.")
