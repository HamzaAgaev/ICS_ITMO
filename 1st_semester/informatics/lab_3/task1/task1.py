import re

smile_pattern = re.compile(r"=-{P")
print("Смайлик: =-{P")

with open("tests1.txt", "r") as read_file:
    all_tests_str = read_file.read()

all_tests_list = all_tests_str.split("\n")

for i in range(len(all_tests_list)):
    test_str = all_tests_list[i]
    match_test = re.findall(smile_pattern, test_str)
    print(f"Количество смайликов в {i+1} строке: {len(match_test)}")