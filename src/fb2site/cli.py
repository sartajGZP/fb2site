from argparse import ArgumentParser
from pathlib import Path

from .convert import convert_export
from .facebook import parse_posts
from .inspector import inspect_export


def main():
    parser = ArgumentParser(
        prog="fb2site",
        description="Convert Facebook exports into static websites.",
    )

    subparsers = parser.add_subparsers(dest="command", required=True)

    # inspect command
    inspect_parser = subparsers.add_parser(
        "inspect",
        help="Inspect a Facebook export.",
    )

    inspect_parser.add_argument(
        "path",
        help="Path to the Facebook export directory.",
    )

    # convert command
    convert_parser = subparsers.add_parser(
        "convert",
        help="Convert a Facebook export into Markdown.",
    )

    convert_parser.add_argument(
        "input",
        help="Path to the Facebook export directory.",
    )

    convert_parser.add_argument(
        "output",
        help="Directory where Markdown files will be written.",
    )

    args = parser.parse_args()

    if args.command == "inspect":
        result = inspect_export(Path(args.path))

        if result.directory_exists:
            print("✓ Directory exists")

        if result.is_facebook_export:
            print("✓ Facebook export detected")

        if result.export_type:
            print(f"Type: {result.export_type}")

        if result.errors:
            print("\nErrors:")
            for error in result.errors:
                print(f"  - {error}")

        posts = parse_posts(Path(args.path))
        print(f"\nParsed {len(posts)} posts")
        print(posts[0])

    elif args.command == "convert":
        convert_export(
            Path(args.input),
            Path(args.output),
        )
