'''
Created on Sep 26, 2018

@author: grant
'''
class Surface(object):
    '''
    classdocs
    '''


    def __init__(self, surface):
        '''
        Constructor
        '''
        self.surface = surface
        
        
    def surfaceVertices(self):
        l = len(self.surface)
        toReturn = []
        for i in range(l):
            toReturn.append(self.surface[i])
            
        return toReturn
    
    def surfaceForVRML(self):
        l = len(self.surface)
        toReturn = ""
        for i in range(l):
            toReturn += str(self.surface[i]) + ", "
        toReturn += "-1, "
        
        return toReturn