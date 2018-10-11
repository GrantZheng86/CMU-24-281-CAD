'''
Created on Sep 26, 2018

@author: grant

'''

import numpy as np
from HW4.Vertex import *
from HW4.Surface import *

# Some functions here -----------------------------------------------
def vertexToVRML(vertecis):
    toReturn = "point["
    l = len(vertecis)
    for i in range(l):
        curr = vertecis[i]
        toReturn += curr.coordinateForVRML()
        if (i != l - 1):
            toReturn += ", "
    toReturn += "]"
    return toReturn

def surfaceToVRLM(surfaces):
    l = len(surfaces)
    toReturn = "coordIndex["
    for i in range(l):
        curr = surfaces[i]
        toReturn += curr.surfaceForVRML()
        
    toReturn += "\n        ]\n"
    return toReturn
        
def generateArrow(direction):
    toReturn = "Shape { \n"
    toReturn += "    appearance Appearance {\n"
    toReturn += "        material Material {\n"
    toReturn += "            diffuseColor 0 0 1\n"
    toReturn += "        }\n"
    toReturn += "    }\n"
    toReturn += "    geometry Extrusion {\n"
    toReturn += "        crossSection [1 0, 0 -1, -1 0, 0 1, 1 0] \n"
    toReturn += "            scale [0.05 0.05, 0.05 0.05, 0.2 0.2, 0 0]\n"
    if (direction == "x"):
        toReturn +="            spine [0 0 0, 10 0 0, 10 0 0, 10.5 0 0]\n"
    elif (direction == "y"):
        toReturn +="            spine [0 0 0, 0 10 0,0 10  0, 0 10.5 0]\n"
    elif (direction == "z"):
        toReturn +="            spine [0 0 0, 0 0 10,0 0 10, 0 0 10.5]\n" 
        
    toReturn += "    }\n"
    toReturn += "}\n"
    
    return toReturn 
# End Functions ------------------------------------------------------
triceratops = open("triceratops.dat").read().split()
vertexArray = []
surfaceArray = []
stopRead = 0;
i = 0;

while(stopRead == 0):
    if (triceratops[i] == "v"):
        x = float(triceratops[i + 1])
        y = float(triceratops[i + 2])
        z = float(triceratops[i + 3])
        currVertex = Vertex(x,y,z)
        vertexArray.append(currVertex)
    
    else:
        stopRead = 1;
    i += 4
    
i -= 4;

while(i < len(triceratops)):
    if (triceratops[i] == "f"):
        i += 1;
        currSurfacePoints = []
        while (i < len(triceratops) and triceratops[i] != "f"):
            currSurfacePoints.append(int(triceratops[i]))
            i += 1
            
        currSurface = Surface(currSurfacePoints)
        surfaceArray.append(currSurface)
        
nVertex = len(vertexArray)
nSurface = len(surfaceArray)

triceratopsVRML = open("triceratops2.wrl","w+")
vrmlString = "#VRML V2.0 utf8\n\n"
vrmlString += "Background{ \n    skyColor 1 1 1\n} \n \n"
vrmlString += "NavigationInfo{\n    type \"EXAMINE\" \n}\n\n"

# Shape one
vrmlString += "Shape{\n    geometry IndexedFaceSet{\n        coord Coordinate{\n"
vrmlString += "            " + vertexToVRML(vertexArray) + "\n" + "        }\n"
vrmlString += "        " + surfaceToVRLM(surfaceArray) + "\n    }\n"
vrmlString += "     appearance Appearance { \n"
vrmlString += "        material Material { \n"
vrmlString += "            diffuseColor 1 0 0 \n"
vrmlString += "        } \n"
vrmlString += "    }\n"
vrmlString += "} \n"

# Draw Triad
vrmlString += generateArrow("x") + generateArrow("y") + generateArrow("z")

# Texts
vrmlString += """
Transform {\n
translation 0 7 0 \n
children Shape {\n
    geometry Text {\n
        string ["24-681: Computer Aided Design",\n
            "Qiaojie Zheng (qiaojiez@andrew.cmu.edu)" ] \n
        fontStyle FontStyle{\n
            family "SANS"\n
            justify "MIDDLE"\n
            style "BOLD"\n
            size 0.5\n 
        }\n
    }\n
    appearance Appearance {\n
        material Material {\n
            diffuseColor 1 0 0\n
         }\n
    }\n
}\n 
}\n
"""
stretch = np.matrix([[2, 0, 0, 0],
                     [0, 1, 0, 0],
                     [0, 0, 1, 0],
                     [0, 0, 0, 1]])
sinVal = np.sin(np.pi/4)
cosVal = np.cos(np.pi/4)
uv = 1/np.sqrt(3)
rotation = np.matrix([[cosVal + uv * uv *(1 - cosVal), uv * uv*(1 - cosVal) - uv * sinVal, uv * uv*(1 - cosVal) + uv*sinVal, 0],
                      [(1 - cosVal)*uv*uv+ sinVal*uv,  cosVal + (1 - cosVal)*uv*uv,(1 - cosVal)*uv * uv - sinVal*uv,0],
                      [(1 - cosVal)*uv*uv - sinVal*uv, (1-cosVal)*uv*uv + sinVal*uv, cosVal + uv*uv*(1 - cosVal),0],
                      [0,0,0,1]])

totalMatrix = np.matmul(stretch, rotation)

mutatedVertex = []

for i in range(nVertex):
    oldVertex = (vertexArray[i]).returnMatrixValue()
    newVertex = np.matmul(totalMatrix,oldVertex)
    newVertex = Vertex(newVertex.item(0),newVertex.item(1),newVertex.item(2))
    mutatedVertex.append(newVertex)
    
    
vrmlString += "Shape{\n    geometry IndexedFaceSet{\n        coord Coordinate{\n"
vrmlString += "            " + vertexToVRML(mutatedVertex) + "\n" + "        }\n"
vrmlString += "        " + surfaceToVRLM(surfaceArray) + "\n    }\n"
vrmlString += "     appearance Appearance { \n"
vrmlString += "        material Material { \n"
vrmlString += "            diffuseColor 0 1 0 \n"
vrmlString += "        } \n"
vrmlString += "    }\n"
vrmlString += "} \n"

triceratopsVRML.write(vrmlString)


