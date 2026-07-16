from dataclasses import dataclass, field


@dataclass
class Inspection:
    directory_exists: bool = False
    is_facebook_export: bool = False
    export_type: str | None = None
    version: str | None = None

    warnings: list[str] = field(default_factory=list)
    errors: list[str] = field(default_factory=list)
