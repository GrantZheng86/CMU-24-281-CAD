'''
Created on Sep 16, 2018

@author: grant
'''

import numpy as np



birdPoints = open("bird.txt","r").read().split()
nRow = int(len(birdPoints) / 2)
pointsArray = np.zeros((nRow,2))

for i in range(nRow):
    pointsArray[i][0] = float(birdPoints[i * 2])
    pointsArray[i][1] = float(birdPoints[i * 2 + 1])

area = 0
for i in range(nRow):
    vectorOne = [pointsArray[i][0], pointsArray[i][1]]
    if (i + 1 >= nRow):
        nxt = 0
    else:
        nxt = i + 1
        
    vectorTwo = [pointsArray[nxt][0], pointsArray[nxt][1]]
    crossResult = np.cross(vectorOne, vectorTwo)
    area += crossResult / 2

toDisplay = "Problem Set 3-1 \n"
toDisplay += "Qiaojie Zheng \n"
toDisplay += "File Name: bird.txt \n"
toDisplay += "# of vertices:" + str(nRow) + "\n"
toDisplay += "Area:" + str(area)


print(area)

