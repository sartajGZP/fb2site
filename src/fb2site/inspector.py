from pathlib import Path

from .models import Inspection

EXPECTED_DIRS = [
    "your_facebook_activity",
    "personal_information",
]


def inspect_export(path: Path) -> Inspection:
    result = Inspection()

    if not path.exists():
        result.errors.append(f"{path} does not exist")
        return result

    if not path.is_dir():
        result.errors.append(f"{path} is not a directory")
        return result

    result.directory_exists = True

    missing = [
        directory
        for directory in EXPECTED_DIRS
        if not (path / directory).exists()
    ]

    if missing:
        result.errors.extend(
            f"Missing directory: {directory}"
            for directory in missing
        )
        return result

    result.is_facebook_export = True
    result.export_type = "facebook"

    return result
