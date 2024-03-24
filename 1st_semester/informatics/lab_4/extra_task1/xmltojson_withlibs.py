import json
import xmltodict

INPUT_FILE = "schedule.xml"
OUTPUT_FILE = "schedule.json"
with open(INPUT_FILE, "r", encoding="UTF-8") as XMLFile:
    XMLText = XMLFile.read()
    dataDict = xmltodict.parse(XMLText)

with open(OUTPUT_FILE, "w", encoding="UTF-8") as JSONFile:
    jsonData = json.dumps(dataDict, ensure_ascii=False, indent=4)
    JSONFile.write(jsonData)