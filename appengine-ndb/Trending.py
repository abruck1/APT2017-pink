from google.appengine.api import users
from commonMethods import *
from ndb_model import *

import os
import jinja2
import webapp2

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.dirname(__file__)),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

# [START trending_page]
class TrendingPage(webapp2.RequestHandler):

    def get(self):
        
        email = 'youremail@gmail.com'
        template_values = {
            'page': "Trending",
            'email': email,
        }
        url, url_linktext, user = logout_func(self)
        template_values['url'] = url
        template_values['url_linktext'] = url_linktext
        template_values['user'] = user

        template = JINJA_ENVIRONMENT.get_template('templates/trending.html')
        self.response.write(template.render(template_values))

# [END trending_page]
