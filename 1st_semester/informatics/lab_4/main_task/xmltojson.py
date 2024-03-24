class XMLDefinition:
    def __init__(self, definition: str) -> None:
        self.definition = definition
        self.__getNameAndAttributes()

    def __getNameAndAttributes(self) -> None:
        definitionAttributes = self.definition[1:-1].split()
        self.name = definitionAttributes[0]
        for i in range(1, len(definitionAttributes)):
            nameAndValue = definitionAttributes[i].split("=")
            if nameAndValue[1][0] == '"' and nameAndValue[1][-1] == '"':
                nameAndValue[1] = nameAndValue[1][1:-1]
            self.__setattr__(nameAndValue[0], nameAndValue[1])
    
    def __eq__(self, other) -> bool:
        for key in vars(self).keys():
            if self.__getattribute__(key) != other.__getattribute__(key):
                return False
        return True

class XMLToJSONParser:
    def __init__(self, filename: str, file_encoding:str="UTF-8") -> None:
        if filename.endswith(".xml"):
            self.XMLText = ""
            self.__XMLTextStripped = ""
            with open(filename, "r", encoding=file_encoding) as XMLFile:
                self.XMLText = XMLFile.read()
        else:
            raise AssertionError("Input file must be a XML-file.")
    
    def parse(self) -> str:
        return self.__decomposeXMLToString()

    def __decomposeXMLToString(self) -> str:
        XMLNotStripped = self.XMLText.split("\n")
        self.__XMLTextStripped = "".join(list(map(str.strip, XMLNotStripped)))
        result_str = ""
        i = 0
        StackXMLDefinition = []
        while i < len(self.__XMLTextStripped):
            CurrentText = self.__XMLTextStripped[i:]
            if CurrentText.startswith("<") and not (CurrentText.startswith("</") or CurrentText.startswith("<?")):
                endDefinitionIndex = CurrentText.find(">")
                CurrentXMLDefinition = XMLDefinition(CurrentText[:endDefinitionIndex + 1])
                i += endDefinitionIndex
                addkey = ""
                if len(StackXMLDefinition) > 0:
                    if StackXMLDefinition[-1].type == "dict":
                        addkey = f'"{CurrentXMLDefinition.name}": '
                if CurrentXMLDefinition.type == "dict":
                    result_str += addkey + "{"
                elif CurrentXMLDefinition.type == "arr":
                    result_str += addkey + "["
                else:
                    if len(StackXMLDefinition) > 0:
                        AfterDefinitionText = CurrentText[endDefinitionIndex + 1:]
                        endItemIndex = AfterDefinitionText.find("</")
                        add = ''
                        if CurrentXMLDefinition.type == "str":
                            add = '"'
                        if StackXMLDefinition[-1].type == "dict":
                            result_str += f'"{CurrentXMLDefinition.name}": {add}{AfterDefinitionText[:endItemIndex]}{add}, '
                        elif StackXMLDefinition[-1].type == "arr":
                            result_str += f"{add}{AfterDefinitionText[:endItemIndex]}{add}, "
                        i += endItemIndex
                StackXMLDefinition.append(CurrentXMLDefinition)
            elif CurrentText.startswith("</"):
                LastXMLDefinition = StackXMLDefinition.pop()
                endOfClosingIndex = CurrentText.find(">")
                if LastXMLDefinition == CurrentXMLDefinition and LastXMLDefinition.name == CurrentText[2:endOfClosingIndex]:
                    if LastXMLDefinition.type == "dict":
                        result_str = result_str[:-2]
                        result_str += "}, "
                    elif LastXMLDefinition.type == "arr":
                        result_str = result_str[:-2]
                        result_str += "], "
                    if len(StackXMLDefinition) > 0:
                        CurrentXMLDefinition = StackXMLDefinition[-1]
                else:
                    print(result_str)
                    raise AssertionError("Failed to decompose: there are unclosed tags in XML file.")
            i += 1
        result_str = result_str[:-2]
        return result_str

INPUT_FILE = "schedule.xml"
OUTPUT_FILE = "schedule.json"
with open(OUTPUT_FILE, "w", encoding="UTF-8") as JSONFile:
    XMLJSONParser = XMLToJSONParser(INPUT_FILE)
    JSON_str = XMLJSONParser.parse()
    JSONFile.write(JSON_str)