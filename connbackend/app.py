#!/usr/bin/env python

import os
import logging
import hashlib

from google.appengine.dist import use_library
use_library('django', '1.2') 


from google.appengine.ext import webapp
from google.appengine.ext.webapp import template
from google.appengine.ext.webapp.util import run_wsgi_app
from google.appengine.ext.webapp import util
from django.utils import simplejson as json

from copy import deepcopy
from models import Menu, MenuItem, UIProfile, Restaurant
from constants import *

AUTH_ENABLED = False
PASSCODE = '4a0e36be6e7d439f83ef8aa8d3f4a40f'


"""
{"menu_id": "70d92ac71e1e4b1f", 
"restaurant_id": "19968b3ba550485dbfd8bf431c6851ef", 
"menu_name": "Updated NEWMENU2", 
"ui_profile": 
    {
        "logo_url": "http:\/\/www.virginialogos.com\/Portals\/57ad7180-c5e7-49f5-b282-c6475cdb7ee7\/Food.jpg",                     
        "color": "black", 
        "menu": null,
        "profile_id": "0b945dcd26db419b", 
        "template": "classy", 
        "font": "Helvetica"
    }, 
"restaurant_name": "Updated TEST", 
"menuitems": 
    {
        "Drink": 
        [
            {
                "category": "Drink", 
                "menuitem_id": "6cfb79454bc2446d", 
                "description": "UPDATED", 
                "image": "This is a sample menu Item", 
                "price": 11.0, 
                "name": "Starter Item 2"
            }
        ], 
        "Appy": 
        [
            {
                "category": "Updated Appy", 
                "menuitem_id": "18aedbf42a8b41ad", 
                "description": "", 
                "image": "Updated This is a sample menu Item", 
                "price": 1000.0, 
                "name": "Updated Starter Item 1"
            }
        ]
    }

}
"""

def ModelsFromDoc(jsonString):
    try:
        obj = json.loads(jsonString)
        menu_id = obj['menu_id']
        rest_id = obj['restaurant_id']
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
        #logging.info(str(obj))
        #Create or Update menuitems
    
        menu_items_dict = obj['menuitems']
        logging.info(type(menu_items_dict))
        for category in menu_items_dict.keys():
            category_list = menu_items_dict[category]
            logging.info(type(category_list))
            for menu_item_dict in category_list:
                logging.info(type(menu_item_dict))
                menuitem = MenuItem.get_by_id(menu_item_dict['menuitem_id'])
                if menuitem:
                    menuitem.name = menu_item_dict['name']
                    menuitem.category = category
                    menuitem.price = menu_item_dict['price']
                    menuitem.image = menu_item_dict['image']
                    menuitem.description = menu_item_dict['description']
                else:
                    menuitem=MenuItem.create(   menu_item_dict['name'],
                                                menu, 
                                                menu_item_dict['price'], 
                                                category,  
                                                menu_item_dict['image'], 
                                                menu_item_dict['description'])
                menuitem.put()
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
    message = request.get('message')
    timestamp = request.get('timestamp')
    request_hash = request.get('message_hash')
    secret_key = Restaurant.get_by_id(request.get('restaurant_id')).secret_key
    message_hash = hashlib.sha1( message+secret_key+timestamp ).hexdigest(  )
    return message_hash==request_hash
    

# Home Page Handler
# message == doc
class MainHandler(webapp.RequestHandler):
    def get(self):
        if AUTH_ENABLED and not verifyMessage(self.request): 
            self.response.out.write('AUTH FAILED')
            return
        
        rest_id = self.request.get('restaurant_id')
        rest = Restaurant.get_by_id(rest_id)
        if not rest:
            self.response.out.write('Restaurant does not exist')
            return
            
        menu_id = Menu.get_menus_by_rest_id(rest_id)[0].menu_id
        doc = DocFromModels(rest_id, menu_id)
        doc_obj = json.loads(doc)
        self.response.out.write(template.render('templates/index.html', doc_obj))

class Preview(webapp.RequestHandler):
    def get(self):
        if AUTH_ENABLED and not verifyMessage(self.request): 
            self.response.out.write('AUTH FAILED')
            return
        
        rest_id = self.request.get('restaurant_id')
        rest = Restaurant.get_by_id(rest_id)
        if not rest:
            self.response.out.write('Restaurant does not exist')
            return
            
        doc = self.request.get('doc')
        doc_obj = json.loads(doc)
        self.response.out.write(template.render('templates/index.html', doc_obj))

#check
#curl -d "doc=%7B%22menu_id%22%3A%20%2270d92ac71e1e4b1f%22%2C%20%22restaurant_id%22%3A%20%2219968b3ba550485dbfd8bf431c6851ef%22%2C%20%22menu_name%22%3A%20%22Updated%20NEWMENU2%22%2C%20%22ui_profile%22%3A%20%7B%22logo_url%22%3A%20%22http%3A%5C%2F%5C%2Fwww.virginialogos.com%5C%2FPortals%5C%2F57ad7180-c5e7-49f5-b282-c6475cdb7ee7%5C%2FFood.jpg%22%2C%20%22color%22%3A%20%22black%22%2C%20%22menu%22%3A%20null%2C%20%22profile_id%22%3A%20%220b945dcd26db419b%22%2C%20%22template%22%3A%20%22classy%22%2C%20%22font%22%3A%20%22Helvetica%22%7D%2C%20%22restaurant_name%22%3A%20%22Updated%20TEST%22%2C%20%22menuitems%22%3A%20%7B%22Drink%22%3A%20%5B%7B%22category%22%3A%20%22Drink%22%2C%20%22menuitem_id%22%3A%20%226cfb79454bc2446d%22%2C%20%22description%22%3A%20%22UPDATED%22%2C%20%22menu%22%3A%20null%2C%20%22image%22%3A%20%22This%20is%20a%20sample%20menu%20Item%22%2C%20%22price%22%3A%2011.0%2C%20%22name%22%3A%20%22Starter%20Item%202%22%7D%5D%2C%20%22Appy%22%3A%20%5B%7B%22category%22%3A%20%22Updated%20Appy%22%2C%20%22menuitem_id%22%3A%20%2218aedbf42a8b41ad%22%2C%20%22description%22%3A%20%22%22%2C%20%22menu%22%3A%20null%2C%20%22image%22%3A%20%22Updated%20This%20is%20a%20sample%20menu%20Item%22%2C%20%22price%22%3A%201000.0%2C%20%22name%22%3A%20%22Updated%20Starter%20Item%201%22%7D%5D%7D%7D" http://localhost:8087/menu/update
#message=doc
class Update(webapp.RequestHandler):
    def post(self):
        if AUTH_ENABLED and not verifyMessage(self.request): 
            self.response.out.write('AUTH FAILED')
            return
        message = self.request.get('doc')
        if ModelsFromDoc(message):
            self.response.out.write('Update Successful')
        else:
            self.response.out.write('ERROR: Incomplete or Malformed doc')

# check
# curl -d "restaurant_id=19968b3ba550485dbfd8bf431c6851ef&menu_name=NEWMENU2" http://localhost:8087/menu/create        
#messages = restaurant_id+menu_name
class CreateMenu(webapp.RequestHandler):
    def post(self):
        if AUTH_ENABLED and not verifyMessage(self.request): 
            self.response.out.write('AUTH FAILED')
            return
        rest_id = self.request.get('restaurant_id')
        menu_name = self.request.get('menu_name')
        
        rest = Restaurant.get_by_id(rest_id)
        if not rest:  
            self.response.out.write('Invalid restaurant_id')
            return
        
        if Menu.get_menus_by_rest_id(rest_id):
            self.resonse.out.write('Cant create another menu for this restaurant.  It already has one')
            return
    
        menu = Menu.create(menu_name, rest)
        uiProfile = UIProfile.create(menu)
        menu_item_1 = MenuItem.create('Starter Item 1', menu, 10.00, 'Appy', 'This is a sample menu Item')
        menu_item_2 = MenuItem.create('Starter Item 2', menu, 11.00, 'Drink','This is a sample menu Item')
        
        self.response.out.write(DocFromModels(rest_id, menu.menu_id))
# check
# curl -d "menu_id=b4c5fb73c0984b73" http://localhost:8087/menu/delete 
#message = menu_id         
class DeleteMenu(webapp.RequestHandler):
    def post(self):
        if AUTH_ENABLED and not verifyMessage(self.request): 
            self.response.out.write('AUTH FAILED')
            return
        menu_id = self.request.get('menu_id')
       
        if Menu.delete_by_id(menu_id):
            self.response.out.write('Menu Successfully Deleted')
        else:
            self.response.out.write('ERROR: Could not delete')

#check
#curl -d "restaurant_id=02673146be10439280ec624bc031815d" http://localhost:8087/restaurant/delete 
#message = restaurant_id
class DeleteRestaurant(webapp.RequestHandler):
    def post(self):
        if AUTH_ENABLED and not verifyMessage(self.request): 
            self.response.out.write('AUTH FAILED')
            return
        restaurant_id = self.request.get('restaurant_id')
        if Restaurant.delete_by_id(restaurant_id):
            self.response.out.write('Menu Successfully Deleted')
        else:
            self.response.out.write('ERROR: Could not delete')

# check
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


#check
#http://localhost:8087/menu?menu_id=70d92ac71e1e4b1f&restaurant_id=19968b3ba550485dbfd8bf431c6851ef
#http://localhost:8087/menu?menu_id=70d92ac71e1e4b1f&restaurant_id=19968b3ba550485dbfd8bf431c6851ef&message=70d92ac71e1e4b1f19968b3ba550485dbfd8bf431c6851ef&timestamp=1320992880.21&message_hash=6d05f6f4dff876605f94431f77d5473e053a9c21
#import hashlib
#secret_key = 07c421f66e184424a9b38fb48cdfeddf
#timestamp = 1320992880.21
# message = menu_id+restaurant_id
# all requests must include restaurant_id
class GetMenu(webapp.RequestHandler):
    def get(self):
        if AUTH_ENABLED and not verifyMessage(self.request): 
            self.response.out.write('AUTH FAILED')
            return
        rest_id = self.request.get('restaurant_id')
        menu_id = self.request.get('menu_id')
        self.response.out.write(DocFromModels(rest_id, menu_id))
        

appRoute = webapp.WSGIApplication( [    ('/restaurant/create',      CreateRestaurant),
										('/restaurant/delete',      DeleteRestaurant),
										('/menu/create',            CreateMenu),
										('/menu/update',            Update),
										('/menu/delete',            DeleteMenu),
										('/menu',                   GetMenu),
										('/preview',                Preview),
										('/', MainHandler)
										], debug=True)
										
def main():
    run_wsgi_app(appRoute)

if __name__ == '__main__':
    main()
