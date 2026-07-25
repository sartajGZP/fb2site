from dataclasses import dataclass, field
from datetime import datetime
from pathlib import Path

@dataclass
class ConvertOptions:
    output_dir: Path

    content_dir: Path = Path("fb-export")

    image_root: Path = Path("assets/img/fb")
    video_root: Path = Path("assets/video/fb")
    layout: str = "layouts/base.njk"
