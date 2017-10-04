import os

import jinja2
import webapp2

from connexus.common import *

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.PackageLoader('connexus', 'templates'),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

# [START error_page]
class Error(webapp2.RequestHandler):

    def get(self):
        
        template_values = {
            'page': "Connex.us",
        }
        url, url_linktext, user = logout_func(self)
        template_values['url'] = url
        template_values['url_linktext'] = url_linktext
        template_values['user'] = user

        template = JINJA_ENVIRONMENT.get_template('error.html')
        self.response.write(template.render(template_values))

# [END error_page]
