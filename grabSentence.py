from bs4 import BeautifulSoup
import urllib2
import random

def GetSentence(_word):
	"""
		Get the sentence from the generated web

		Arg:	The word that you want to generate
		Ret:	The sentenses result
	"""
	#url = "http://www.wordhippo.com/what-is/sentences-with-the-word/conservation.html"
	url = "http://www.wordhippo.com/what-is/sentences-with-the-word/" \
			+ _word + ".html"

	# Get the soup object by the url
	html_data = urllib2.urlopen(url).read()
	soup = BeautifulSoup(html_data, 'html.parser')

	# Get the specific tag's sentence
	rows = soup.find_all('div', {"class": "relatedwords"})
	return Split(rows[0].text)


def detectNewLine(string):
	"""
		Detect the new line position
		This function is deprecated.
	"""
	print "-----start detect-----"
	print type(string.text)
	for c in string.text:
		#print c
		if c == '\n':
			print "!"

def Split(string):
	"""
		Get the sentence list

		Arg:	The mess string result
		Ret:	The normal sentences list
	"""
	start = 0
	strList = []
	_string = ""
	for end in range(len(string)):
		_string += string[end]
		if string[end] == '.':
			strList.append(_string)
			_string = ""
			start = end+1
	# Show result
	"""
	for i in range(len(strList)):
		print i, " :\t", strList[i]
	"""
	return strList

if __name__ == "__main__":
	while True:
		_string = raw_input("Enter the word that you want to practise:\t")
		_list = GetSentence(_string)
		print _list[random.randint(0, len(_list))]
