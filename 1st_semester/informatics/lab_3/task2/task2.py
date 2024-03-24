import re

def editTime(text: str) -> str:
    before_time = r"(?P<before>^|\s)"           # символы, которые могут находиться перед временем
    time = r"([0-1]\d|2[0-3])(:[0-5]\d)?(:[0-5]\d)"
    after_time = r"(?P<after>(\W$|\W\s|\s|$))"    # символы, которые могут находиться после времени

    time_pattern = re.compile(before_time + time + after_time)

    match_test = re.search(time_pattern, text)
    if match_test != None:
        result = re.sub(time_pattern, match_test.group("before") + r"(TBD)" + match_test.group("after"), text)
    else:
        result = text

    return result

with open("tests2.txt", "r", encoding="utf-8") as read_file:
    all_tests_list = read_file.read().split("\n")
    for i in range(len(all_tests_list)):
        test_str = all_tests_list[i]
        res = editTime(test_str)
        print(res)