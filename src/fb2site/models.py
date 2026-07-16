from dataclasses import dataclass, field


@dataclass
class ExportStats:
    posts: int = 0
    comments: int = 0
    reactions: int = 0
    photos: int = 0
    videos: int = 0
    albums: int = 0


@dataclass
class Inspection:
    directory_exists: bool = False
    is_facebook_export: bool = False
    export_type: str | None = None
    version: str | None = None

    stats: ExportStats = field(default_factory=ExportStats)

    warnings: list[str] = field(default_factory=list)
    errors: list[str] = field(default_factory=list)

@dataclass
class Post:
    id: str | None
    timestamp: datetime
    title: str
    body: str
    links: list[str] = field(default_factory=list)
    photos: list[Path] = field(default_factory=list)
    videos: list[Path] = field(default_factory=list)
