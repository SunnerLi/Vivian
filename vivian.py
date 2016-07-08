from telebot import types
from Parser import *
import telebot
import random
import json

bot = telebot.TeleBot("181718772:AAHJiobdYJvo8iwDqj2h4fovfO8Vn69ZwhA")

# Specific variable
practiceFlag = 0
wordPairs = None
answerIndex = None

# Bot handler
@bot.message_handler(commands=['who'])
def send_welcome(message):
    desc = "I'm vivian, the tutor of the english."
    bot.send_message(message.chat.id, desc)
    
@bot.message_handler(commands=['help'])
def send_welcome(message):
    desc = "There're some commands you can use:\n1. who: tell you who is me\n2. help: tell you the usage command\n3. practice: you can start to practice with vivian\n4. stop: you can stop practice and rest\n\nThe Bot is created by sunnerli"
    bot.send_message(message.chat.id, desc)
    
@bot.message_handler(commands=['practice'])
def send_welcome(message):
    global practiceFlag
    desc = "OK, Let's start to talk!\nIf you want to stop practice, you should type /stop command."
    bot.send_message(message.chat.id, desc)
    guess(message)
    practiceFlag = 1
    
@bot.message_handler(commands=['stop'])
def send_welcome(message):
    global practiceFlag
    desc = "Stop practice."
    markup = types.ReplyKeyboardHide()
    bot.send_message(message.chat.id, desc, reply_markup=markup)
    
@bot.message_handler(func=lambda message: True)
def echo_all(message):
    global practiceFlag
    global wordPairs
    global answerIndex
    bot.reply_to(message, "The answer: "+wordPairs[answerIndex][1])    
    markup = types.ReplyKeyboardHide()
    
    # Practice result
    if practiceFlag == 1:
        if judge(message.text) == 1:
            print "the same"
            sticker = open('./img/yes.webp', 'rb')
            bot.send_sticker(message.chat.id, sticker, reply_markup=markup)
            sticker.close()
        else:
            print "differnet"
            sticker = open('./img/no.webp', 'rb')
            bot.send_sticker(message.chat.id, sticker, reply_markup=markup)
            sticker.close()
        guess(message)
    else:
        bot.send_message(message.chat.id, "done.")
    
# Function define    
def guess(message):
    """
        Guess function implementation
    """
    global answerIndex
    
    # Determine four choice
    c1 = random.randint(0, len(wordPairs)-1)
    c2 = random.randint(0, len(wordPairs)-1)
    c3 = random.randint(0, len(wordPairs)-1)
    c4 = random.randint(0, len(wordPairs)-1)
    qi = random.choice([c1, c2, c3, c4])
    
    # Form button
    print "c1: ", c1, "\tc2: ", c2
    r1 = [(wordPairs[c1][1]), (wordPairs[c2][1])]
    r2 = [wordPairs[c3][1], wordPairs[c4][1]]
    keyboard = [r1,r2]
    news_keyboard = {'keyboard': keyboard}
    bot.send_message(message.chat.id, wordPairs[qi][0]+" ? ", None, None, json.dumps(news_keyboard))
    answerIndex = qi
    
def judge(txt):
    """
        Judge if the two chinese string are the same
    """
    global answerIndex
    global wordPairs
    print answerIndex
    print "len(txt):", len(txt), "\tlen(wordPairs[answerIndex][1])", wordPairs[answerIndex][1]
    if not len(txt) == len(wordPairs[answerIndex][1]):
        return 0
    for i in range(len(txt)):
        if not ord(txt[i]) == ord(wordPairs[answerIndex][1][i]):
            return 0
    return 1 
    

# Main function
if __name__ == '__main__':    
    # Load word pair
    p = Parser()
    wordPairs = p.load()
    print wordPairs
    
    # Conduct bot
    bot.polling(none_stop=True)