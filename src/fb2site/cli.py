from argparse import ArgumentParser
from pathlib import Path

from .convert import convert_export
from .takeout import parse_posts
from .inspector import inspect_export
from .takeout_input import InputSource


def main():
    parser = ArgumentParser(
        prog="fb2site",
        description="Convert Facebook exports into static websites.",
    )

    subparsers = parser.add_subparsers(dest="command", required=True)

    # inspect
    inspect_parser = subparsers.add_parser(
        "inspect",
        help="Inspect a Facebook Takeout ZIP.",
    )

    inspect_parser.add_argument(
        "path",
        help="Path to the Facebook Takeout ZIP.",
    )

    # convert
    convert_parser = subparsers.add_parser(
        "convert",
        help="Convert a Facebook export into Markdown.",
    )

    convert_parser.add_argument(
        "--format",
        choices=["takeout", "group"],
        required=True,
        help="Input format.",
    )

    convert_parser.add_argument(
        "input",
        help="Path to the Facebook Takeout ZIP or group JSON file.",
    )

    convert_parser.add_argument(
        "output",
        help="Directory where Markdown files will be written.",
    )

    args = parser.parse_args()

    if args.command == "inspect":
        source = InputSource(Path(args.path))

        try:
            result = inspect_export(source.path)

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

            posts = parse_posts(source.path)
            print(f"\nParsed {len(posts)} posts")

            if posts:
                print(posts[0])

        finally:
            source.cleanup()

    elif args.command == "convert":
        if args.format == "takeout":
            source = InputSource(Path(args.input))

            try:
                convert_export(
                    source.path,
                    Path(args.output),
                    args.format,
                )
            finally:
                source.cleanup()

        else:
            convert_export(
                Path(args.input),
                Path(args.output),
                args.format,
            )


if __name__ == "__main__":
    main()
