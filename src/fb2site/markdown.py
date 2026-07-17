def escape_markdown(text: str) -> str:
    """Escape user text so it renders literally in Markdown."""

    if not text:
        return ""

    text = text.replace("\\", "\\\\")
    text = text.replace("#", r"\#")
    text = text.replace("*", r"\*")
    text = text.replace("_", r"\_")
    text = text.replace(">", r"\>")
    text = text.replace("`", r"\`")

    return text
