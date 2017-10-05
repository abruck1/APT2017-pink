import os

import jinja2
import webapp2

from connexus.common import *

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.PackageLoader('connexus', 'templates'),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

# [START trending_page]
class Trending(webapp2.RequestHandler):

    def get(self):
        
        user = users.get_current_user()
        if user:
            email = user.email()
        else:
            email = "Not logged in"
        template_values = {
            'page': "Trending",
            'email': email,
        }
        url, url_linktext, user = logout_func(self)
        template_values['url'] = url
        template_values['url_linktext'] = url_linktext
        template_values['user'] = user

        template = JINJA_ENVIRONMENT.get_template('trending.html')
        self.response.write(template.render(template_values))

# [END trending_page]
