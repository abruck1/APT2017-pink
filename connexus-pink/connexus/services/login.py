import os
import sys

import jinja2
import webapp2

from connexus.common import *

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.PackageLoader('connexus', 'templates'),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

# google auth needs this
CLIENT_ID = "521418538274-ful52avc9n4dm8isfbv7igrq54k3teh4.apps.googleusercontent.com"

# [START home_page]
class Login(webapp2.RequestHandler):

    def get(self):

        template_values = {
            'page': "Connex.us",
            'CLIENT_ID': CLIENT_ID,
        }
        url, url_linktext, user = logout_func(self, user_text='Enter Connex.us!')
        template_values['url'] = url
        template_values['url_linktext'] = url_linktext
        template_values['user'] = user

        template = JINJA_ENVIRONMENT.get_template('login.html')
        self.response.write(template.render(template_values))
# [END home_page]
