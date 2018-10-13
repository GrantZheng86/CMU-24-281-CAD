'''
Created on Oct 12, 2018

@author: grant
'''
import numpy as np
class HelperMethods(object):
    
    '''
    This class contains helper methods used in the main function.
    This class must be included in the folder in order to run the main
    function without any problem
    '''


    def __init__(self, params):
        '''
        Dummy Constructor
        '''

    @staticmethod
    def readDataString(dataString):
        '''
        This class reads a string containing int or float separate by
        either commas or spaces, returns a numpy array
        '''   
        if "," not in dataString:
            toReturn = np.array(list(map(float, dataString.split(" "))))
        else:
            toReturn = np.array(list(map(float, dataString.split(","))))
        
        return toReturn
    
    @staticmethod 
    def readDataString2(dataString):
        '''
        This class handles spreading numbers when ";" is presenting in the 
        string spread in the string
        '''
        xyzTogether = list(map(float, dataString.split(";")))
        toReturn = []
        for i in range(len(xyzTogether)):
            temp = HelperMethods.readDataString(xyzTogether[i])
            toReturn.append(temp)
            
        return toReturn
        

    