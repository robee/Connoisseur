#!/usr/bin/env python

import os
import logging
from google.appengine.ext import webapp
from google.appengine.ext.webapp import template
from google.appengine.ext.webapp.util import run_wsgi_app
from google.appengine.ext.webapp import util
from google.appengine.api import users


from models import User
from constants import *


#What the Template App Still Needs

#- Basic Security Features
#- Data validation decorator
#- Sign in 
#- Must sign in decorator




# Home Page Handler
class MainHandler(webapp.RequestHandler):
    def get(self):
        
        content_template_values = {
           
        }
       
        self.response.out.write(RenderFullPage('index.html', content_template_values))



# Gets
class GetMethod(webapp.RequestHandler):
    def get(self):
        return 0



# Posts
class PostMethod(webapp.RequestHandler):
    def post(self):
        return 0


        




appRoute = webapp.WSGIApplication( [('/page1', PageHandler1),
										('/page2', PageHandler2),
										('/save', SaveUser),
										('/', MainHandler)
										], debug=True)
										
def main():
    run_wsgi_app(appRoute)

if __name__ == '__main__':
    main()
