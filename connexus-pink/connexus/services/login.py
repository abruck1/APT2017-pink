import os
import sys

import jinja2
import webapp2

from connexus.common import *
# from webapp2_extras import auth
# from google.oauth2 import id_token
# from google.auth.transport import requests

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.PackageLoader('connexus', 'templates'),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

# google auth needs this
# CLIENT_ID = "726264805787-r04pldtjtkr88b255pfu802do7h6b0r0.apps.googleusercontent.com"


# [START home_page]
class Login(webapp2.RequestHandler):

    def get(self):

        user = users.get_current_user()

        if user:
            self.redirect("/view")

        template_values = {
            'page': "Connex.us",
        }
        url, url_linktext, user = logout_func(self, user_text='Enter Connex.us!')
        template_values['url'] = url
        template_values['url_linktext'] = url_linktext
        template_values['user'] = user

        template = JINJA_ENVIRONMENT.get_template('login.html')
        self.response.write(template.render(template_values))

    # def post(self):
    #
    #     user_email = self.request.get("idtoken")
    #     print("id_token 1 {}".format(user_email))
    #
    #     # auth.AuthStore.create_auth_token(user_email)
    #     self.redirect('/view?user=' + user_email)

# [END home_page]
