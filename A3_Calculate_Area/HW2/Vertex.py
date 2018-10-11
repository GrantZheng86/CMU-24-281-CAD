'''
Created on Sep 16, 2018

@author: grant
'''
import numpy as np

class Vertex(object):
    '''
    classdocs
    '''


    def __init__(self, x, y, z):
        '''
        Constructor
        '''
        self.x = x;
        self.y = y;
        self.z = z;
        
        
    def printCoordinate(self):
        toPrint = "[" + str(self.x) + "," + str(self.y) + "," +  str(self.z) + "] \n"
        print(toPrint)
        
    def returnValue(self):
        return np.array([self.x, self.y, self.z])