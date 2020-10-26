#!/usr/bin/env python

def fibnum(n):
    i,j = 0, 1
    while True:
        t = i+j
        i,j  = j, t 
        yield t      
        
if __name__=="__main__":
    x = fibnum(1000)
    for i in range(0,100):
        print (x.next())