
#!/usr/bin/python
# -*- coding: iso-8859-15 -*-
import re
text = """The leader of the most prominent group in the US peddling potentially lethal industrial bleach as a miracle cure for coronavirus 
wrote to Donald Trump at the White House this week."""
words = re.compile("[^\w]").split(text)
lengths = ((word.strip(), len(word)) for word in words)
print(max(lengths, key=lambda x: x[1]))

