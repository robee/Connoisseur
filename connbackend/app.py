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

"""
{
     "menu_id": "09jv903rjv",
     "restaurant_id":"34534f34f",
     "restaurant_name":"NAME",
     
     "menu_name": "Full Menu",
     "template": "classy",
     "ui_profile":
     {
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



 class Restaurant(db.Model):
     restaurant_id = db.StringProperty(required=True)
     name = db.StringProperty(required=True)

     def get_by_id(r_id):
     def save(name_p, menu_p=None):
        

 class Menu(db.Model):
     menu_id = db.StringProperty(required=True)
     name = db.StringProperty(required=True)
     template = db.StringProperty()
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
     menu = db.ReferenceProperty(Menu, required=True)
     color = db.StringProperty()
     font = db.StringProperty()

"""

def DocFromModels(rest_id, menu_id):
    
    rest = Restaurant.get_by_id(rest_id)
    menu = Menu.get_by_id(menu_id)
    ui_profile = UIProfile.get_by_menu(menu)
    menuitems = MenuItem.get_by_menu(menu)
    
    
    obj = {}
    obj['menu_id']=menu.menu_id
    obj['restaurant_id']=rest.restaurant_id
    obj['restaurant_name']=rest.name
    obj['menu_name']= menu.name
    obj['ui_profile']= deepcopy(ui_profile.vars())
    obj['menuitems']={}
    
    for menuitem in menuItems:
        category = menuitem.category
        obj['menuitems'][category] = list(obj['menuitems'][category]).append(menuitem)
    
    return json.dumps(obj)
    

def MenuFromDoc(jsonString):
    obj = json.loads(jsonString)
    menu_id = obj['menu_id']
    rest_id = obj['rest_id']
    
    menu = Menu.get_by_+id
    

# Home Page Handler
class MainHandler(webapp.RequestHandler):
    def get(self):
        
        content_template_values = {
           
        }
       
        self.response.out.write(template.render('templates/index.html', content_template_values))


# Home Page Handler
class UpdateMenu(webapp.RequestHandler):
    def get(self):

        content_template_values = {

        }

        self.response.out.write(template.render('templates/index.html', content_template_values))
# Home Page Handler
class CreateMenu(webapp.RequestHandler):
    def post(self):

        content_template_values = {

        }

        self.response.out.write(template.render('templates/index.html', content_template_values))

# Home Page Handler
class DeleteMenu(webapp.RequestHandler):
    def post(self):

        content_template_values = {

        }

        self.response.out.write(template.render('templates/index.html', content_template_values))


# Home Page Handler
class UpdateRestaurant(webapp.RequestHandler):
    def post(self):

        content_template_values = {

        }

        self.response.out.write(template.render('templates/index.html', content_template_values))
        
# Home Page Handler
class CreateRestaurant(webapp.RequestHandler):
    def post(self):
        message_hash = hashlib.sha1( message+secret_key+message_key ).hexdigest(  )
        
        content_template_values = {

        }

        self.response.out.write(template.render('templates/index.html', content_template_values))







appRoute = webapp.WSGIApplication( [    ('/restaurant/create',  CreateRestaurant),
										('/restaurant/update',  UpdateRestaurant),
										('/menu/create',        CreateMenu),
										('/menu/update',        UpdateMenu),
										('/menu/delete',        DeleteMenu),
										('/menu/',              Menu),
										('/', MainHandler)
										], debug=True)
										
def main():
    run_wsgi_app(appRoute)

if __name__ == '__main__':
    main()
