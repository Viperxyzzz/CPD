import time

M_AR = 300
M_BR = 300
pha = [[0] * M_AR for i in range(M_AR)]
phb = [[0] * M_AR for i in range(M_AR)]
phc = [[0] * M_AR for i in range(M_AR)]

def main():
    for i in range(0, M_AR):
        for j in range(0, M_AR):
            pha[i][j] = 1.0


    for i in range(0, M_BR):
        for j in range(0, M_BR):
            phb[i][j] = i+1.0


    start_time = time.time()

    for i in range(0, M_AR):
        for j in range(0, M_BR):
            temp = 0
            for k in range(0, M_AR):
                temp += pha[i][k] * phb[k][j]
            phc[i][j] = temp
    return start_time

def OnMultLine():
    for i in range(0, M_AR):
        for j in range(0, M_AR):
            pha[i][j] = 1.0


    for i in range(0, M_BR):
        for j in range(0, M_BR):
            phb[i][j] = i+1.0


    start_time = time.time()

    for j in range(0, M_BR):
        for i in range(0, M_AR):
            temp = 0
            for k in range(0, M_AR):
                temp += pha[i][k] * phb[k][j]
            phc[i][j] = temp
    return time.time() - start_time

run_time = OnMultLine()
print("--- %s seconds ---" % ( run_time))
