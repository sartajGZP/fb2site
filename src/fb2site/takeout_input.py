from pathlib import Path
import tempfile
import zipfile


class InputSource:
    def __init__(self, path: Path):
        self._tmp = None

        if path.is_file() and path.suffix.lower() == ".zip":
            self._tmp = tempfile.TemporaryDirectory()
            with zipfile.ZipFile(path) as z:
                z.extractall(self._tmp.name)

            self.path = Path(self._tmp.name)
        else:
            self.path = path

    def cleanup(self):
        if self._tmp:
            self._tmp.cleanup()
