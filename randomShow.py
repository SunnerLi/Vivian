from downloadSentence import *
import random
import os


# Load the dict
List = Read()

# Start!
while True:
    os.system('cls' if os.name == 'nt' else 'clear')

    # Determine the index and print
    wordIndex = random.randint(0, List["header"]["number"]-1)
    sentenceIndex = random.randint(0, len(List["word"][wordIndex]["sentences"])-1)
    print "Sentence:\n", List["word"][wordIndex]["sentences"][sentenceIndex]
    print "\n\nWord:\n\n", List["word"][wordIndex]["english"]

    # Print the direction
    print "\n\n"
    print "[-- Type enter key to get the next sentence --]"
    print "[-- Ctrl+D to escape ..................... --]"
    try:
        c = raw_input()
    except EOFError:
        break