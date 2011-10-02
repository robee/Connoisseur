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
       
        self.response.out.write(template.render('templates/index.html', content_template_values))






appRoute = webapp.WSGIApplication( [
										('/', MainHandler)
										], debug=True)
										
def main():
    run_wsgi_app(appRoute)

if __name__ == '__main__':
    main()
