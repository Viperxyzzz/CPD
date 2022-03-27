import time


def func(num):
    M_AR = num
    M_BR = num
    return OnMultLine(M_AR, M_BR)


def OnMult(M_AR, M_BR):
    pha = [[0] * M_AR for i in range(M_AR)]
    phb = [[0] * M_AR for i in range(M_AR)]
    phc = [[0] * M_AR for i in range(M_AR)]
    percentages = [i for i in range(M_AR // 10, M_AR, M_AR // 10)]
    for i in range(0, M_AR):
        for j in range(0, M_AR):
            pha[i][j] = 1.0

    for i in range(0, M_BR):
        for j in range(0, M_BR):
            phb[i][j] = i + 1.0

    start_time = time.time()

    for i in range(0, M_AR):
        if (i in percentages): print(i)
        for j in range(0, M_BR):
            temp = 0
            for k in range(0, M_AR):
                temp += pha[i][k] * phb[k][j]
            phc[i][j] = temp
    return time.time() - start_time


def OnMultLine(M_AR, M_BR):
    pha = [[0] * M_AR for i in range(M_AR)]
    phb = [[0] * M_AR for i in range(M_AR)]
    phc = [[0] * M_AR for i in range(M_AR)]
    percentages = [i for i in range(M_AR // 10, M_AR, M_AR // 10)]
    for i in range(0, M_AR):
        for j in range(0, M_AR):
            pha[i][j] = 1.0

    for i in range(0, M_BR):
        for j in range(0, M_BR):
            phb[i][j] = i + 1.0

    start_time = time.time()

    for j in range(0, M_BR):
        if (j in percentages): print(j)
        for i in range(0, M_AR):
            temp = 0
            for k in range(0, M_AR):
                temp += pha[i][k] * phb[k][j]
            phc[i][j] = temp
    return time.time() - start_time


def main():
    for i in range(1000, 3001, 400):
        run_time = func(i)
        print("--- %s seconds ---" % (run_time))


main()
