'''
Created on Oct 12, 2018

@author: grant
'''

class Vertexs(object):
    '''
    classdocs
    '''


    def __init__(self, curr, left = None, right = None, up = None, down = None):
        '''
        Constructor
        '''
        self.curr   = curr
        self.left   = left
        self.right  = right
        self.up     = up
        self.down   = down
        
    #def calculateNormal(self):
        