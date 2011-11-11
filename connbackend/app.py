#!/usr/bin/env python

import os
import logging
import hashlib

from google.appengine.ext import webapp
from google.appengine.ext.webapp import template
from google.appengine.ext.webapp.util import run_wsgi_app
from google.appengine.ext.webapp import util
from django.utils import simplejson as json

from copy import deepcopy
from models import Menu, MenuItem, UIProfile, Restaurant
from constants import *

AUTH_ENABLED = False
PASSCODE = 'aaaaa'


"""
{
     "menu_id": "%s",
     "restaurant_id":"%s",
     "restaurant_name":"%s",
     "menu_name": "%s",
     "ui_profile":
     {
         "profile_id": "3454gse",
         "template": "classy",
         "color": "red",
         "font": "helvetica"
         "logo_url": "http://i.imgur.com/eCcSx.jpg",
     },
     "menuitems":
     {
         "dinner": 
         [      
            {
                   "menuitem_id": "soiv303g",
                   "name": "Pizza",
                   "price": 330,
                   "image": "http://i.imgur.com/eCcSx.jpg",
                   "description":"Delicious pizza ice cream"
             },
             {
                    "menuitem_id": "soiv303g",
                    "name": "Pizza",
                    "price": 330,
                    "image": "http://i.imgur.com/eCcSx.jpg",
                    "description":"Delicious pizza ice cream"
              }
         ],
         "appetizer": 
         [
            {   
                "menuitem_id": "3ivmn3rio",
                "name": "Burger",
                "price": 2330,
                "image": "http://i.imgur.com/Eua8M.png",
                "description":"Burgers are really good"
            },
            {
                  "menuitem_id": "3ivmn3rio",
                  "name": "Burger",
                  "price": 2330,
                  "image": "http://i.imgur.com/Eua8M.png",
                  "description":"Burgers are really good"
            }
          ]  
     }
 }
"""
"""

 class Restaurant(db.Model):
     restaurant_id = db.StringProperty(required=True)
     name = db.StringProperty(required=True)

     def get_by_id(r_id):
     def save(name_p, menu_p=None):
        

 class Menu(db.Model):
     menu_id = db.StringProperty(required=True)
     name = db.StringProperty(required=True)
     restaurant = db.ReferenceProperty(Restaurant, required=True)

     def get_by_id(m_id):
     def get_menus_by_rest_id(rest_id):
     def save(name_p, restaurant_p, template_p="", ):
      


 class MenuItem(db.Model):
     menuitem_id = db.StringProperty(required=True)
     menu = db.ReferenceProperty(Menu, required=True)
     name = db.StringProperty(required=True)
     category = db.StringProperty()
     price = db.FloatProperty(required=True)
     image = db.StringProperty()
     description = db.StringProperty()
     
     def get_by_id(i_id):
     def save(name_p, menu_p, category_p="", price_p=100.0, image_p="", description_p=""):


 class UIProfile(db.Model):
     profile_id = db.StringProperty(required=True)
     template = db.StringProperty()
     menu = db.ReferenceProperty(Menu, required=True)
     color = db.StringProperty()
     font = db.StringProperty()

"""


def MenuFromDoc(jsonString):
    try:
        obj = json.loads(jsonString)
        menu_id = obj['menu_id']
        rest_id = obj['rest_id']
        profile_id = obj['ui_profile']['profile_id']
    
        # Create or Update Rest
        rest = Restaurant.get_by_id(rest_id)
        rest.name = obj['restaurant_name']
    
        #Create or Update Menu
        menu = Menu.get_by_id(menu_id)
    
        menu.name = obj['menu_name']
        menu.resturant = rest
    
        #Create or Update UI Profile
        ui_profile = UIProfile.get_by_id(profile_id)
        ui_profile.template = obj['ui_profile']['template']
        ui_profile.color = obj['ui_profile']['color']
        ui_profile.font = obj['ui_profile']['font']
        ui_profile.logo_url = obj['ui_profile']['logo_url']
    
        #Create or Update menuitems
               # 
               # for category in obj['menuitems']
               #     category_name = category.key
               #     for menu_item_dict in category:
               #         menuitem = MenuItem.get_by_id(menu_item_dict['menuitem_id'])
               #         menuitem.menu = menu
               #         menuitem.name = menu_item_dict['name']
               #         menuitem.category = category_name
               #         menuitem.price = menu_item_dict['price']
               #         menuitem.image = menu_item_dict['image']
               #         menuitem.description = menu_item_dict['description']
               #         menuitem.put()
        ui_profile.put()
        menu.put()
        rest.put()
        return True
    except:
        return False

def DocFromModels(rest_id, menu_id):
    try:
        rest = Restaurant.get_by_id(rest_id)
        menu = Menu.get_by_id(menu_id)
        ui_profile = UIProfile.get_by_menu(menu)
        menuitems = MenuItem.get_by_menu(menu)
       
        grab_vars = lambda item:  deepcopy(vars(item)['_entity'])
        
        
        obj = {}
        obj['menu_id']=menu.menu_id
        obj['restaurant_id']=rest.restaurant_id
        obj['restaurant_name']=rest.name
        obj['menu_name']= menu.name
        obj['ui_profile']= grab_vars(ui_profile)
        obj['ui_profile']['menu']=None
        obj['menuitems']={}
    
        for menuitem in menuitems:
            category = menuitem.category
            menu_item_dict = grab_vars(menuitem)
            menu_item_dict['menu']=None
            if obj['menuitems'].has_key(category):
                obj['menuitems'][category] = list(obj['menuitems'][category]).append(menu_item_dict)
            else:
                obj['menuitems'][category] = [menu_item_dict]
    
        return json.dumps(obj)
        #return str(obj)
    except:
        return 'ERROR: Invalid restaurant_id or menu_id'

def verifyMessage( request ):
    #message, secret_key, timestamp, request_hash
    message = request.get('doc')
    timestamp = request.get('timestamp')
    request_hash = request.get('message_hash')
    doc = json.loads(message)
    secret_key = Restaurant.get_by_id(doc['restaurant_id']).secret_key
    message_hash = hashlib.sha1( message+secret_key+timestamp ).hexdigest(  )
    return message_hash==request_hash
    

# Home Page Handler
class MainHandler(webapp.RequestHandler):
    def get(self):
        
        content_template_values = {
           
        }
       
        self.response.out.write(template.render('templates/index.html', content_template_values))



class Update(webapp.RequestHandler):
    def post(self):
        if AUTH_ENABLED and not verifyMessage(self): 
            self.response.out.write('AUTH FAILED')
            return
        message = request.get('doc')
        if MenuFromDoc(message):
            self.response.out.write('Update Successful')
        else:
            self.response.out.write('ERROR: Incomplete or Malformed doc')


# curl -d "restaurant_id=8a72d9b62ad84d1d9c8f9927eea8e07c&menu_name=NEWMENU" http://localhost:8087/menu/create        
class CreateMenu(webapp.RequestHandler):
    def post(self):
        if AUTH_ENABLED and not verifyMessage(self): 
            self.response.out.write('AUTH FAILED')
            return
        rest_id = self.request.get('restaurant_id')
        menu_name = self.request.get('menu_name')
        
        rest = Restaurant.get_by_id(rest_id)
        if not rest:  
            self.response.out.write('Invalid restaurant_id')
            return
            
    
        menu = Menu.create(menu_name, rest)
        uiProfile = UIProfile.create(menu)
        menu_item_1 = MenuItem.create('Starter Item 1', menu, 10.00, 'Appy', 'This is a sample menu Item')
        menu_item_2 = MenuItem.create('Starter Item 2', menu, 11.00, 'Drink','This is a sample menu Item')
        
        self.response.out.write(DocFromModels(rest_id, menu.menu_id))
        
class DeleteMenu(webapp.RequestHandler):
    def post(self):
        if AUTH_ENABLED and not verifyMessage(self): 
            self.response.out.write('AUTH FAILED')
            return
        message = self.request.get('doc')
        doc = json.loads(message)
        if Menu.delete_by_id(doc['menu_id']):
            self.response.out.write('Menu Successfully Deleted')
        else:
            self.response.out.write('ERROR: Could not delete')

class DeleteRestaurant(webapp.RequestHandler):
    def post(self):
        if AUTH_ENABLED and not verifyMessage(self): 
            self.response.out.write('AUTH FAILED')
            return
        message = request.get('doc')
        doc = json.loads(message)
        if Restaurant.delete_by_id(doc['menu_id']):
            self.response.out.write('Menu Successfully Deleted')
        else:
            self.response.out.write('ERROR: Could not delete')

# curl -d "name=TEST" http://localhost:8087/restaurant/create
class CreateRestaurant(webapp.RequestHandler):
    def post(self):
        auth_code = self.request.get('auth_code')
        if auth_code != PASSCODE and AUTH_ENABLED: 
            self.response.out.write("AUTH FAILED")
            return
            
        rest = Restaurant.create(self.request.get('name'))
        return_obj = {
            'secret_key':rest.secret_key,
            'restaurant_id':rest.restaurant_id
        }
        
        self.response.out.write(json.dumps(return_obj))
        
class GetMenu(webapp.RequestHandler):
    def get(self):
        if AUTH_ENABLED and not verifyMessage(self): 
            self.response.out.write('AUTH FAILED')
            return
        message = request.get('doc')
        doc = json.loads(message)
        rest_id = doc['restaurant_id']
        menu_id = doc['menu_id']
        self.response.out.write(DocFromModels(rest_id, menu_id))
        

appRoute = webapp.WSGIApplication( [    ('/restaurant/create',  CreateRestaurant),
										('/restaurant/delete',  DeleteRestaurant),
										('/menu/create',        CreateMenu),
										('/a/update',             Update),
										('/menu/delete',        DeleteMenu),
										('/menu/',              GetMenu),
										('/', MainHandler)
										], debug=True)
										
def main():
    run_wsgi_app(appRoute)

if __name__ == '__main__':
    main()
