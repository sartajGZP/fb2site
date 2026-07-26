from linkify_it import LinkifyIt

_linkify = LinkifyIt()

def linkify(text: str) -> str:
    if not text:
        return text

    matches = list(_linkify.match(text) or [])
    if not matches:
        return text

    result = []
    last = 0

    for match in matches:
        result.append(text[last:match.index])
        url = match.url
        result.append(f"<{url}>")
        last = match.last_index

    result.append(text[last:])

    return "".join(result)
