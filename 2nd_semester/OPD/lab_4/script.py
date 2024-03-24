with open("got.txt", "r") as file:
    got = file.read()
    got = got.split("\n")
    for i in range(len(got)):
        got[i] = "WORD 0x" + got[i][5:]

got = "\n".join(got)
with open("out.txt", "w") as out:
    out.write(got)