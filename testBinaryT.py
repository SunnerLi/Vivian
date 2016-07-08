#coding=utf-8

from binaryT import *

print ' --- Create Tree ---'
t = Tree()
t.insert('bus', "公車")
t.insert('app', "應用程式")
t.insert('fox', "狐狸")
t.insert('apple', "蘋果")
t.insert('hire', "僱用")
t.insert('ox', "公牛")
t.show()

print ' --- Delete Node ---'
t.delete('hire')
t.delete('bus')
t.show()

print " --- After Dump--- "
wholeList = t.dump()
for i in wholeList:
    print i.en, '\t', i.ch.decode('utf8')