#coding=utf-8
#!/usr/bin/env python

import sys
from PyQt4.QtGui import *
from PyQt4 import QtCore
from binaryT import *
from Parser import *

class WordWindow(QWidget):
    def __init__(self, parent=None):
        """
            Create the layout and add the connection
        """
        super(WordWindow, self).__init__(parent)
        self.p = Parser()
        self.pair = self.p.load()
        self.createLayout()
        
    def createLayout(self):
        """
            Create the layout
        """
        # Set Font object
        font = QFont("Arial", 20)
        
        # Create chinese line
        self.chineseLine = QLineEdit()
        self.chineseLine.setFont(font)
        chineseTag = QLabel('中文'.decode('utf8'))
        chineseTag.setFont(font)
        hLayout1 = QHBoxLayout()
        hLayout1.addWidget(chineseTag)
        hLayout1.addWidget(self.chineseLine)
        
        # Create english line
        self.englishLine = QLineEdit()
        self.englishLine.setFont(font)
        englishTag = QLabel('Eng '.decode('utf8'))
        englishTag.setFont(font)
        hLayout2 = QHBoxLayout()
        hLayout2.addWidget(englishTag)
        hLayout2.addWidget(self.englishLine)
        
        # Create Button( 新增 & 關閉 )
        addBtn = QPushButton("&n新增".decode('utf8'))
        addBtn.setFont(font)
        addBtn.clicked.connect(self.addBtnClick)
        closeBtn = QPushButton("關閉".decode('utf8'))
        closeBtn.setFont(font)
        closeBtn.clicked.connect(self.closeWindow)
        hLayout3 = QHBoxLayout()
        hLayout3.addWidget(addBtn)
        hLayout3.addWidget(closeBtn)

        
        # Combine the layout
        wholeLayout = QVBoxLayout()
        wholeLayout.addLayout(hLayout1)
        wholeLayout.addLayout(hLayout2)
        wholeLayout.addLayout(hLayout3)
        
        # Create whole widget
        self.resize(400, 300)
        self.move(500, 400)
        self.setWindowTitle("單字增減視窗".decode('utf8'))
        self.setLayout(wholeLayout)
        
    def addBtnClick(self):
        """
            Add the word pair to the tree
        """
        en = self.englishLine.text()
        ch = self.chineseLine.text()
        chString = QtCore.QString(ch).toUtf8()
        print "新增配對：[ ", en, "\t", chString, " ]"
        self.pair.append([str(en), str(chString)])
        self.englishLine.clear()
        self.chineseLine.clear()
        
    def closeWindow(self):
        """
            Record the result and close the window
        """
        if not self.pair == []:
            t = Tree()
            for i in self.pair:
                if not i[1] == "":
                    t.insert(i[0], i[1])
                else:
                    t.delete(i[0])
            self.pair = t.dump()
            
            # Tree structure to list structure
            _list = []
            for i in self.pair:
                print i.en, i.ch
                _list.append([i.en, i.ch])
            self.p.save(_list)
        self.close()
        
        
if __name__ == '__main__':
    app = QApplication(sys.argv)
    
    widget = WordWindow()
    widget.show()
    
    app.exec_()