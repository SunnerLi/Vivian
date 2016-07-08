#coding=utf-8

# Constant
LEFT = 0
RIGHT = 1

# Node definition
class Node():
    en = None
    ch = None
    leftChild = None
    rightChild = None
    def __init__(self, en, ch):
        """
            Constructor
        """
        self.en = en
        self.ch = ch
        
    def insert(self, en, ch):
        """
            Insert a node
        """
        if not self.en == en:
            if self.direction(en) == LEFT:
                if self.leftChild == None:
                    self.leftChild = Node(en, ch)
                else:
                    self.leftChild.insert(en, ch)
            else:
                if self.rightChild == None:
                    self.rightChild = Node(en, ch)
                else:
                    self.rightChild.insert(en, ch)
        else:
            self.ch = ch
                
    def delete(self, en):
        """
            Delete the node
        """
        if en == self.en:
            self.en = None
            self.ch = None
            print 'remove'
        else:
            if not self.leftChild == None:
                self.leftChild.delete(en)
            if not self.rightChild == None:
                self.rightChild.delete(en)
                
    def show(self):
        """
            Inorder tree walk
        """
        if not self.leftChild == None:
            self.leftChild.show()
        if not self.en == None:
            print self.en, "\t", self.ch.decode('utf8')
        if not self.rightChild == None:
            self.rightChild.show()
            
    def direction(self, en):
        """
            Determine the direction to go
        """
        for i in range(len(self.en)):
            if ord(en[i]) < ord(self.en[i]):
                return LEFT
            elif ord(en[i]) > ord(self.en[i]):
                return RIGHT
            else:
                if i == (min(len(en), len(self.en)) - 1):
                    return RIGHT
                else:
                    i += 1
                    
    def depth(self, h=0):
        """
            Get the depth of the node
        """
        if (not self.leftChild == None) and (not self.rightChild == None):
            return max(self.leftChild.depth(h+1), self.rightChild.depth(h+1))
        elif self.leftChild == None and self.rightChild == None:
            return h+1
        elif self.leftChild == None:
            return self.rightChild.depth(h+1)
        elif self.rightChild == None:
            return self.leftChild.depth(h+1)
            
    def dump(self, wordList):
        """
            Store the whole element by inorder tree walk
        """
        if not self.leftChild == None:
            wordList = self.leftChild.dump(wordList)
        if not self.en == None:
            wordList.append(self)
        if not self.rightChild == None:
            wordList = self.rightChild.dump(wordList)
        return wordList   
            
# Tree definition( include one Node object )
class Tree():
    root = None
    def __init__(self):
        pass
        
    def insert(self, en, ch):
        """
            Insert a node
        """
        if self.root == None:
            self.root = Node(en, ch)
        else:
            self.root.insert(en, ch)
            
    def delete(self, en):
        """
            Delete a node
        """
        if not en == self.root.en:
            if not self.root.leftChild == None:
                self.root.leftChild.delete(en)
            if not self.root.rightChild == None:
                self.root.rightChild.delete(en)
        else:
            self.root.en = None
            self.root.ch = None
            
    def show(self):
        """
            Inorder tree walk
        """
        self.root.show()
        
    def depth(self):
        """
            Get the depth of the tree
        """
        if self.root == None:
            return 0;
        else:
            return self.root.depth()
            
    def dump(self, wordList=[]):
        """
            Store the whole tree by inorder tree walk
        """
        if not self.root.leftChild == None:
            wordList = self.root.leftChild.dump(wordList)
        if not self.root.en == None:
            wordList.append(self.root)
        if not self.root.rightChild == None:
            wordList = self.root.rightChild.dump(wordList)
        return wordList
