'''
Created on Oct 12, 2018

@author: grant
'''



from tkinter import *
import tkinter.simpledialog as db
import tkinter.messagebox as mb
import threading 
import os
import numpy as np
from HelperMethods import *
from Vertex import *
from sympy.printing.theanocode import dim_handling

def calculatedirection(vertexArray,dim):
    p0 = vertexArray[0]
    p1 = vertexArray[1]
    diff = np.subtract(p0, p1)
    
    if ( diff[0] <= 0.0001 ):
        return 1
    else:
        return 0
    
def handleConvertingVertex(startNum, endNum, storage, vertexArray):
    l = endNum - startNum
    
    for i in range(l):
        storage[i + startNum] = Vertex(vertexArray[i + startNum])
        
def handleConnectingVertex(newVertexArray, dim, dir):
    l = len(newVertexArray)
    
    for i in range(l):  
        if (dir == 0):
            for i in range(l):
                # If located on the lower boundary
                if (i < dim):
                    if (i == 0):
                        newVertexArray[i].right = newVertexArray[i + 1]
                    elif(i == dim - 1):
                        newVertexArray[i].left = newVertexArray[i - 1]
                    else:
                        newVertexArray[i].left = newVertexArray[i - 1]
                        newVertexArray[i].right = newVertexArray[i + 1]
                        
                    newVertexArray[i].up = newVertexArray[i + dim]
                
                # If located on the upper boundary
                elif(l - dim <= i):
                    if (i == l - dim):
                        newVertexArray[i].right = newVertexArray[i + 1]
                    elif(i == l - 1):
                        newVertexArray[i].left = newVertexArray[i - 1]
                    else:
                        newVertexArray[i].left = newVertexArray[i - 1]
                        newVertexArray[i].right = newVertexArray[i + 1]
                    
                    newVertexArray[i].down = newVertexArray[i - dim]
                
                # If located at the boundaries
                elif(i % dim == 0 or (i + 1) % dim == 0):
                    if (i % dim == 0):
                        newVertexArray[i].right = newVertexArray[i + 1]
                    else:
                        newVertexArray[i].left = newVertexArray[i - 1]
                    
                    newVertexArray[i].up = newVertexArray[i + dim]
                    newVertexArray[i].down = newVertexArray[i - dim]
                
                
                
          
if __name__ == '__main__':
    pass

disp = Tk()
w = Label(disp, text = "24-681 Computer Aided Design HW 7")
w.pack()
mb.showinfo("Enter Instruction", "Please enter numbers, separate with comma")

fileName = db.askstring("File Name", "File Name Including Suffix")
viewPoint = db.askstring("Viewing Point", "View Point in 3D")
patternPlane = db.askstring("Zebra Pattern Plane", "Zebra Pattern Information, p0, a and b")
widthAndSpacing = db.askstring("Zebra Pattern Plane", "Thickness and Spacing for Strip")

fileData = open("../files/" + fileName).read().split("\n")
vertexArray = []
l = len(fileData)

for i in range(l):
    curr = HelperMethods.readDataString(fileData[i])
    vertexArray.append(curr)

viewPoint = HelperMethods.readDataString(viewPoint)
patternPlane = HelperMethods.readDataString2(patternPlane)
dim = int(vertexArray[0][0])
del vertexArray[0]
vertexArrayLength = len(vertexArray)

threadNum = 8
workLoad = int(vertexArrayLength / threadNum)
taskList = [None] * threadNum
newVertexArray = [None] * vertexArrayLength

for i in range(threadNum):
    startNum = i * workLoad
    if (i == threadNum - 1):
        endNum = vertexArrayLength
    else:
        endNum = (i + 1) * workLoad
        
    taskList[i] = threading.Thread(target = handleConvertingVertex, 
                                   args = (startNum, endNum, newVertexArray, vertexArray,))
    taskList[i].start()
    
for i in range(threadNum):
    taskList[i].join()
    
connectedVErtex = handleConnectingVertex(newVertexArray, dim, 0)
print("print")