'''
Created on Sep 16, 2018

@author: grant
'''
import numpy as np

from HW2.Vertex import *
from HW2.Surface import *

points = open("shape.dat","r").read().split()


vertexArray = []
surfaceArray = []
stopRead = 0
i = 0
while(stopRead == 0):
    if (points[i] == "v"):
        x = float(points[i + 1])
        y = float(points[i + 2])
        z = float(points[i + 3])
        currVertex = Vertex(x,y,z)
        vertexArray.append(currVertex)
    else:
        stopRead = 1   
    i += 4
    
i -= 4;    
while(i < len(points)):
    if (points[i] == "f"):
        i += 1;
        currSurface = []
        while(i < len(points) and points[i] != "f"):
            currSurface.append(points[i])
            i += 1
            
    curr = Surface(currSurface)
    surfaceArray.append(curr)
    
nVertex = len(vertexArray)
nSurface = len(surfaceArray)
#print(nVertex)
#print(nSurface)

totalArea = 0
for i in range(nSurface):
    currSurface = surfaceArray[i]
    currVertices = currSurface.surfaceVertices()
    baseVertixIndex = int(currVertices[0])
    baseVertix = vertexArray[baseVertixIndex]
    faceArea = 0
    for j in range(len(currVertices)):
        vertexIndex = int(currSurface.surfaceVertices()[j])
        vectorOne = vertexArray[vertexIndex].returnValue() - baseVertix.returnValue()
        if (j + 1 == len(currVertices) ):
            nxt = baseVertixIndex;
        else:
            nxt = int(currSurface.surfaceVertices()[j + 1])
        vectorTwo = vertexArray[nxt].returnValue() - baseVertix.returnValue()
        currArea = np.cross(vectorOne, vectorTwo)/2
        faceArea += currArea
    totalArea += np.linalg.norm(faceArea)    
print(totalArea)


totalVolume = 0
entireBaseVertices = vertexArray[0]
for i in range(nSurface):
    currSurface = surfaceArray[i]
    currVertices = currSurface.surfaceVertices()
    baseVertixIndex = int(currVertices[0])
    baseVertix = vertexArray[baseVertixIndex]
    singleConeVol = 0
    for j in range(len(currVertices)):
        vertexIndex = int(currSurface.surfaceVertices()[j])
        vectorOne = vertexArray[vertexIndex].returnValue() - baseVertix.returnValue()
        if (j + 1 == len(currVertices) ):
            nxt = baseVertixIndex;
        else:
            nxt = int(currSurface.surfaceVertices()[j + 1])
        vectorTwo = vertexArray[nxt].returnValue() - baseVertix.returnValue()
        vectorThree = baseVertix.returnValue() - entireBaseVertices.returnValue()
        triangleVol = np.dot(np.cross(vectorOne, vectorTwo),vectorThree) / 6
        singleConeVol += triangleVol
    totalVolume += singleConeVol
    
print(totalVolume) 

toDisplay = "Problem Set 3-2 \n \
Qiaojie Zheng \n \
File Name: shape.dat \n \
# of vertices: " + str(nVertex) + "\n "  +  "# of faces : "  + str(nSurface) + "\n " +   "Area:" + str(totalArea) + "\n" 
toDisplay += "Volume:" + str(totalVolume)
    
      
