import unicodedata


def detect_language(text: str) -> str:
    latin = 0
    devanagari = 0

    for ch in text:
        name = unicodedata.name(ch, "")

        if "LATIN" in name:
            latin += 1
        elif "DEVANAGARI" in name:
            devanagari += 1

    if latin > devanagari:
        return "en"

    if devanagari > latin:
        return "hi"

    return "unknown"
