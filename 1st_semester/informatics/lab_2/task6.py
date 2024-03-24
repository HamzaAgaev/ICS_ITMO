import math

MSG_LEN = 7
COUNT_OF_CHECK_BITS = math.ceil(math.log2(MSG_LEN))

full_message = input("Введите все биты сообщения (информационные и проверочные): ")

if len(full_message) != MSG_LEN:
    raise ValueError(f"Длина собщения должна быть равна {MSG_LEN}.")
elif set(full_message) not in  ({"0"}, {"1"}, {"0", "1"}):
    raise ValueError("В сообщении есть лишние символы помимо 0 и 1.")

arr_message = list(map(int, list(full_message)))

SYNDROM = [0] * COUNT_OF_CHECK_BITS
for i in range(COUNT_OF_CHECK_BITS):
    point = 2 ** i
    for j in range(point - 1, MSG_LEN, 2 * point):
        SYNDROM[i] = (SYNDROM[i] + sum(arr_message[j:j+point])) % 2

if SYNDROM != [0] * COUNT_OF_CHECK_BITS:
    error_index = 0
    for i in range(COUNT_OF_CHECK_BITS):
        error_index += SYNDROM[i] * 2 ** i

    power = math.log2(error_index)

    if power == int(power):
        print(f"Ошибка в бите R{int(power) + 1} ({error_index} бит сообщения).")
    else:
        print(f"Ошибка в бите I{error_index-int(power) - 1} ({error_index} бит сообщения).")

    arr_message[error_index - 1] = 1 - arr_message[error_index - 1]
else:
    print("Сообщение было правильным!")

res_info_message = ""

for i in range(1, MSG_LEN + 1):
    if math.log2(i) != int(math.log2(i)):
        res_info_message += str(arr_message[i - 1])

print(f"Правильное сообщение (информационные биты): {res_info_message}.")

res_all_message = "".join(map(str, arr_message))
print(f"Правильное сообщение (все биты): {res_all_message}.")