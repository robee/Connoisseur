#!/usr/bin/env python

import os
import logging
from google.appengine.ext import webapp
from google.appengine.ext.webapp import template
from google.appengine.ext.webapp.util import run_wsgi_app
from google.appengine.ext.webapp import util
from google.appengine.api import users


from models import Menu, MenuItem, UIProfile, Restaurant
from constants import *

"""
{
     "menu_id": "09jv903rjv",
     "name": "Full Menu",
     "template": "classy",
     "ui_profile":
     {
         "color": "red",
         "font": "helvetica",
     },
     "menuitems":
     [
         {
           "menuitem_id": "soiv303g",
           "name": "Pizza",
           "price": 330,
           "image": "http://i.imgur.com/eCcSx.jpg",
           "description":"Delicious pizza ice cream"
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
"""


# Home Page Handler
class MainHandler(webapp.RequestHandler):
    def get(self):
        
        content_template_values = {
           
        }
       
        self.response.out.write(template.render('templates/index.html', content_template_values))

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
    def get(self):

        content_template_values = {

        }

        self.response.out.write(template.render('templates/index.html', content_template_values))

# Home Page Handler
class DeleteMenu(webapp.RequestHandler):
    def get(self):

        content_template_values = {

        }

        self.response.out.write(template.render('templates/index.html', content_template_values))


# Home Page Handler
class UpdateRestaurant(webapp.RequestHandler):
    def get(self):

        content_template_values = {

        }

        self.response.out.write(template.render('templates/index.html', content_template_values))
# Home Page Handler
class CreateRestaurant(webapp.RequestHandler):
    def get(self):

        content_template_values = {

        }

        self.response.out.write(template.render('templates/index.html', content_template_values))







appRoute = webapp.WSGIApplication( [    ('/restaurant/create',  CreateRestaurant),
										('/restaurant/update',  UpdateRestaurant),
										('/menu/create',        CreateMenu),
										('/menu/update',        UpdateMenu),
										('/menu/delete',        DeleteMenu),
										('/', MainHandler)
										], debug=True)
										
def main():
    run_wsgi_app(appRoute)

if __name__ == '__main__':
    main()
