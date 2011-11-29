import cgi
import os
import datetime
import uuid

from google.appengine.ext import db


class Restaurant(db.Model):
    restaurant_id = db.StringProperty(required=True)
    secret_key = db.StringProperty(required=True)
    name = db.StringProperty(required=True)

    @staticmethod
    def create(name):
        rest = Restaurant(rest)
        
    @staticmethod
    def get_by_id(r_id):
        return Restaurant.all().filter('restaurant_id =', r_id).get()
    
    
    @staticmethod
    def delete_by_id(r_id):
        
        rest = Restaurant.get_by_id(r_id)
        menus = Menu.get_menus_by_rest_id(r_id)
        for menu in menus:
            Menu.delete_by_id(menu.menu_id)
        
        
        try:
            rest.delete()
        except:
            return False
            
        return True
    
    @staticmethod
    def create(name_p, menu_p=None):
        rest = Restaurant(restaurant_id = uuid.uuid4().hex, secret_key=uuid.uuid4().hex, name=name_p, menus = menu_p)
        rest.put()
        return rest
    
class Menu(db.Model):
    menu_id = db.StringProperty(required=True)
    name = db.StringProperty(required=True)
    restaurant = db.ReferenceProperty(Restaurant, required=True)


    @staticmethod
    def get_by_id(m_id):
        return Menu.all().filter('menu_id =', m_id).get()
    
    @staticmethod
    def delete_by_id(m_id):
        menu = Menu.get_by_id(m_id)
        
        
        menuitems = MenuItem.get_by_menu(menu)
        for menuitem in menuitems:
            menuitem.delete()
        
        ui_profile = UIProfile.get_by_menu(menu)
        UIProfile.delete_by_id(ui_profile.profile_id)
        try:
            menu.delete()
        except:
            return False
        
        return True
    
    @staticmethod
    def get_menus_by_rest_id(rest_id):
        rest = Restaurant.get_by_id(rest_id)
        return Menu.all().filter('restaurant =', rest).fetch(100)
        
    @staticmethod
    def create(name_p, restaurant_p):
        menu = Menu(menu_id = uuid.uuid4().hex[:16], name=name_p, restaurant=restaurant_p)
        menu.put()
        return menu



class MenuItem(db.Model):
    menuitem_id = db.StringProperty(required=True)
    menu = db.ReferenceProperty(Menu, required=True)
    name = db.StringProperty(required=True)
    category = db.StringProperty()
    price = db.StringProperty(required=True)
    image = db.StringProperty()
    description = db.StringProperty()
    
    
    @staticmethod
    def get_by_menu(menu):
        return MenuItem.all().filter('menu =', menu).fetch(1000)
    
    @staticmethod
    def delete_by_menu(menu):
        menu_items = MenuItem.get_by_menu(menu)
        for menu_item in menu_items:
            menu_item.delete()
        
    @staticmethod
    def delete_by_id(i_id):
        menuitem = MenuItem.get_by_id(i_id)
        
        try:
            menuitem.delete()
        except:
            return False
        
        return True
        
    @staticmethod
    def get_by_id(i_id):
        return MenuItem.all().filter('menuitem_id =', i_id).get()
        
    @staticmethod
    def create(name_p, menu_p, price_p=100.0, category_p="",  image_p="", description_p=""):
        menuitem = MenuItem(menuitem_id = uuid.uuid4().hex[:16], menu=menu_p, name=name_p, price = str(price_p), image = image_p, description=description_p, category=category_p)
        menuitem.put()
        return menuitem


class UIProfile(db.Model):
    profile_id = db.StringProperty(required=True)
    menu = db.ReferenceProperty(Menu, required=True)
    template = db.StringProperty()
    color = db.StringProperty()
    font = db.StringProperty()
    logo_url = db.StringProperty()
    
    @staticmethod
    def create(menu_p, template_p='classy', color_p='black', font_p='Helvetica', logo_url_p='http://www.virginialogos.com/Portals/57ad7180-c5e7-49f5-b282-c6475cdb7ee7/Food.jpg'):
        ui_profile = UIProfile(profile_id=uuid.uuid4().hex[:16], menu=menu_p, template=template_p, color=color_p, font=font_p, logo_url=logo_url_p)
        ui_profile.put()
        return ui_profile
    
    @staticmethod
    def delete_by_id(p_id):
        profile_id = UIProfile.get_by_id(p_id)
        
        try:
            profile_id.delete()
        except:
            return False
        
        return True
        
       
    @staticmethod
    def get_by_id(profile_id):
        return UIProfile.all().filter('profile_id =', profile_id).get()
        
    @staticmethod
    def get_by_menu(menu):
        return UIProfile.all().filter('menu =', menu).get()


    @staticmethod
    def delete_by_menu(menu):
        ui_profile = UIProfile.get_by_menu(menu)
        ui_profile.delete()


