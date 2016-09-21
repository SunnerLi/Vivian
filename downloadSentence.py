from grab1 import *
import json
import time
import csv
import sys

word = []

def Load(csvName="tulip.csv"):
	"""
		Load the word csv file
	"""
	global word
	ptr = open(csvName, 'r')
	for w in csv.DictReader(ptr):
		word.append(w['word'])
	ptr.close()

def Write(fileName="tulip.pair"):
	"""
		Write the sentence and word pair

		The format of header:
		{
			"header":
			{
				"numberOfWord" : <the_number_of_word>
			}
			"word":[
				{
					"englist"   : <english>
					"sentences" : [ <sentences_list> ]
				}
				{
					"englist"   : <english>
					"sentences" : [ <sentences_list> ]
				}
				...
			]
		}
	"""
	global word
	wordList = []
	head = dict(number=len(word))

	# Get the sentence
	count = 0
	for w in word:
		_list = GetSentence(w)
		_word = dict(english=w, sentences=_list)
		wordList.append(_word)
		_statement = "Progress: " + str(count) + " / " + str(len(word)) + "\n"
		sys.stdout.write(_statement) 
		sys.stdout.flush()
		count += 1
		time.sleep(0.5)
	data = dict(header=head, word=wordList)

	# Write into the file
	dataJ = json.dumps(data)
	f = open(fileName, 'w')
	json.dump(dataJ, f)
	f.close()

	# Read from the file
	f = open(fileName, 'r')
	dataJ = json.load(f)
	f.close()
	data = json.loads(dataJ)

	print data["word"]

def Read(fileName="tulip.pair"):
	"""
		Read the JSON pair from the .pair file

		Arg:	The file name of JSON
		Ret:	The dict structure of words
	"""
	# Check if the file exist
	try:
		f = open(fileName, 'r')
	except IOError:
		print ".pair file didn't exist. Reconstruct the file now..."
		Load()
		Write()
		f = open(fileName, 'r')

	# Read from the file
	dataJ = json.load(f)
	f.close()
	data = json.loads(dataJ)
	return data	

def test():
	#word = ["apple", "bird", "cat", "dog"]
	Load()
	Write()