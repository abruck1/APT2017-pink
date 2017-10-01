import os
import sys

import jinja2
import webapp2

from connexus.common import *
# from google.oauth2 import id_token
# from google.auth.transport import requests

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.PackageLoader('connexus', 'templates'),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

# google auth needs this
CLIENT_ID = "726264805787-r04pldtjtkr88b255pfu802do7h6b0r0.apps.googleusercontent.com"


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

    def post(self):

        token = self.request.get("idtoken")
        print("id_token {}".format(token))

        try:
            idinfo = id_token.verify_oauth2_token(token, requests.Request(), CLIENT_ID)

            # Or, if multiple clients access the backend server:
            # idinfo = id_token.verify_oauth2_token(token, requests.Request())
            # if idinfo['aud'] not in [CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3]:
            #     raise ValueError('Could not verify audience.')

            if idinfo['iss'] not in ['accounts.google.com', 'https://accounts.google.com']:
                raise ValueError('Wrong issuer.')

            # If auth request is from a G Suite domain:
            # if idinfo['hd'] != GSUITE_DOMAIN_NAME:
            #     raise ValueError('Wrong hosted domain.')

            # ID token is valid. Get the user's Google Account ID from the decoded token.
            userid = idinfo['sub']
            print(userid)
        except ValueError:
            # Invalid token
            pass

# [END home_page]
