'''
Created on Oct 12, 2018

@author: grant
'''



from tkinter import *
import tkinter.simpledialog as db
import tkinter.messagebox as mb

import numpy as np
from HW7.HelperMethods import HelperMethods


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
for i in range(len(fileData)):
    vertexArray.append(HelperMethods.readDataString(fileData[i]))

viewPoint = np.array(HelperMethods.readDataString(viewPoint))
patternPlane = HelperMethods.readDataString(patternPlane)
p0 = np.array(patternPlane[:3])
a  = np.array(patternPlane[3:6])
b  = np.array(patternPlane[6:9])
widthAndSpacing = HelperMethods.readDataString(widthAndSpacing)
width = widthAndSpacing[0]
spacing = widthAndSpacing[1]

print(b)