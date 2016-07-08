from telebot import types
import telebot
import random
import json


bot = telebot.TeleBot("181718772:AAHJiobdYJvo8iwDqj2h4fovfO8Vn69ZwhA")

@bot.message_handler(commands=['who'])
def send_welcome(message):
    desc = "I'm vivian, the tutor of the english."
    bot.send_message(message.chat.id, desc)
    
@bot.message_handler(commands=['practice'])
def send_welcome(message):
    desc = "OK, Let's start to talk!"
    bot.send_message(message.chat.id, desc)
    
    """
    markup = types.ReplyKeyboardMarkup()
    itembtna = types.KeyboardButton('apple')
    itembtnv = types.KeyboardButton('bird')
    itembtnc = types.KeyboardButton('cat')
    itembtnd = types.KeyboardButton('dog')
    markup.row(itembtna, itembtnv)
    markup.row(itembtnc, itembtnd)
    bot.send_message(message.chat.id, "Choose one letter:", reply_markup=markup)
    """
    r1 = ['a','b','c']
    r2 = ['d','e','f']
    keyboard = [r1,r2]
    news_keyboard = {'keyboard': keyboard}
    bot.send_message(message.chat.id, 'message', None, None, json.dumps(news_keyboard))
    

@bot.message_handler(func=lambda message: True)
def echo_all(message):
    bot.reply_to(message, message.text)    

    markup = types.ReplyKeyboardHide()
    bot.send_message(message.chat.id, "?", reply_markup=markup)
    
    
bot.polling(none_stop=True)