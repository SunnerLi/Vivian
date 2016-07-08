#coding=utf-8

class Parser():
    def __init__(self, fileName='vivian.pair'):
        self.name = fileName
        
    def save(self, pair):
        """
            Save the pairs into file
        """
        fd = open(self.name, 'w+')
        for i in pair:
            fd.write(i[0])
            fd.write("\n")
            try:
                fd.write(i[1])
            except UnicodeEncodeError:
                fd.write(i[1].encode('utf-8'))
            fd.write("\n")
        fd.close()
        
    def load(self):
        """
            Load the whole word pair and return
            
            Struture Example:
            [
                [apple, 蘋果],
                [bird, 鳥]
            ]
        """
        pair = []
        with open(self.name, 'r+') as fd:
            contain = fd.readlines()
            
        for i in range(0, len(contain), 2):
            # read and filter the new line character
            en = contain[i]
            ch = contain[i+1]
            en = en[:len(en)-1]
            ch = ch[:len(ch)-1].decode('utf-8')
            print en
            print ch
            pair.append([en, ch])
        return pair
        
"""     
p = Parser()
p.save([["apple", "蘋果"], ["bus", "公車"]])
#p.save([["apple", "蘋果"]])

llist = p.load()
"""