import cgi
import os
import datetime


from google.appengine.ext import db
from google.appengine.api import users


class Restaurant(db.Model):
    restaurant_id = db.StringProperty(required=True)
    name = db.StringProperty(required=True)
    menu = db.ReferenceProperty(Menu)
    schedule = db.StringProperty()

    @staticmethod
    def get_restaurant_by_id(r_id):
        return Restaurant.all().filter('restaurant_id = ', r_id).get()
        
    @staticmethod
    def save_restaurant(name, menu, schedule=""):
        pass
    
class Menu(db.Model):
    name = db.StringProperty(required=True)
    user = db.ReferenceProperty(User)
    tags = db.ListProperty(db.Category)
    text = db.StringProperty(required=True)
    
    @staticmethod
    def get_by_name(name):
       return SomeEntity.all().filter('name = ', name).get()

