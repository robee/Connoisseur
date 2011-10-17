import cgi
import os
import datetime
import uuid

from google.appengine.ext import db


class Restaurant(db.Model):
    restaurant_id = db.StringProperty(required=True)
    name = db.StringProperty(required=True)

    @staticmethod
    def get_by_id(r_id):
        return Restaurant.all().filter('restaurant_id =', r_id).get()
        
    @staticmethod
    def save(name_p, menu_p=None):
        rest = Restaurant(restaurant_id = uuid.uuid4().hex[:16], name=name_p, menus = menu_p)
        rest.put()
    
class Menu(db.Model):
    menu_id = db.StringProperty(required=True)
    name = db.StringProperty(required=True)
    template = db.StringProperty()
    restaurant = db.ReferenceProperty(Restaurant, required=True)


    @staticmethod
    def get_by_id(m_id):
        return Menu.all().filter('menu_id =', m_id).get()

    @staticmethod
    def get_menus_by_rest_id(rest_id):
        rest = Restaurant.get_by_id(rest_id)
        return Menu.all.filter('restaurant =', rest).fetch(100)
        
    @staticmethod
    def save(name_p, restaurant_p, template_p="", ):
        menu = Menu(menu_id = uuid.uuid4().hex[:16], name=name_p, template = template_p, restaurant=restaurant_p)
        menu.put()



class MenuItem(db.Model):
    menuitem_id = db.StringProperty(required=True)
    menu = db.ReferenceProperty(Menu, required=True)
    name = db.StringProperty(required=True)
    category = db.StringProperty()
    price = db.FloatProperty(required=True)
    image = db.StringProperty()
    description = db.StringProperty()
    
    
    @staticmethod
    def get_by_menu(menu):
        return MenuItems.all().filter('menu =', menu).fetch(1000)
    
    @staticmethod
    def get_by_id(i_id):
        return Menu.all().filter('menuitem_id =', i_id).get()
        
    @staticmethod
    def save(name_p, menu_p, category_p="", price_p=100.0, image_p="", description_p=""):
        menuitem = Menu(menuitem_id = uuid.uuid4().hex[:16], menu=menu_p, name=name_p, price = price_p, image = image_p, description=description_p, category=category_p)
        menuitem.put()


class UIProfile(db.Model):
    profile_id = db.StringProperty(required=True)
    menu = db.ReferenceProperty(Menu, required=True)
    color = db.StringProperty()
    font = db.StringProperty()
    logo_url = db.StringProperty()
   
    @staticmethod
    def get_by_menu(menu):
        return UIProfile.all().filter('menu =', menu).get()





