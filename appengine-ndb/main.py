#!/usr/bin/env python

# Copyright 2016 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# [START imports]
import os

from SignIn import SignIn
from google.appengine.api import users
#from google.appengine.ext import ndb

import jinja2
import webapp2

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.dirname(__file__)),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)
# [END imports]

def logout_func(self):
    user = users.get_current_user()
    if user:
        url = users.create_logout_url(self.request.uri)
        url_linktext = 'Logout'
    else:
        url = users.create_login_url(self.request.uri)
        url_linktext = 'Login'
        user = "Anonymous"
    return (url, url_linktext, user)

# [START home_page]
class HomePage(webapp2.RequestHandler):

    def get(self):
        #client_id = "521418538274-ful52avc9n4dm8isfbv7igrq54k3teh4.apps.googleusercontent.com"

        user = users.get_current_user()
        print("user={}".format(user))
        if user:
            #url = users.create_logout_url(self.request.uri)
            url = "/view"
            url_linktext = 'Enter Connex.us!'
        else:
            url = users.create_login_url(self.request.uri)
            url_linktext = 'Login'
            user = "Anonymous"

        template_values = {
            'page': "Connex.us",
            'user': user,
            'greetings': "Hello!",
            'url': url,
            'url_linktext': url_linktext,
        }

        template = JINJA_ENVIRONMENT.get_template('templates/home.html')
        self.response.write(template.render(template_values))
# [END home_page]
# # [START home_page]
# class HomePage(webapp2.RequestHandler):
#
#     def get(self):
#         #client_id = "521418538274-ful52avc9n4dm8isfbv7igrq54k3teh4.apps.googleusercontent.com"
#
#         user = users.get_current_user()
#         print("user={}".format(user))
#         if user:
#             #url = users.create_logout_url(self.request.uri)
#             url = "/view"
#             url_linktext = 'Enter Connex.us!'
#         else:
#             url = users.create_login_url(self.request.uri)
#             url_linktext = 'Login'
#             user = "Anonymous"
#
#         template_values = {
#             'page': "Connex.us",
#             'user': user,
#             'greetings': "Hello!",
#             'url': url,
#             'url_linktext': url_linktext,
#         }
#
#         template = JINJA_ENVIRONMENT.get_template('templates/home.html')
#         self.response.write(template.render(template_values))
# # [END home_page]

# [START contact_page]
class ContactPage(webapp2.RequestHandler):

    def get(self):
        email = 'youremail@gmail.com'
        template_values = {
            'page': "Connex.us",
            'email': email,
        }

        template = JINJA_ENVIRONMENT.get_template('templates/contact.html')
        self.response.write(template.render(template_values))
# [END contact_page]

# [START create_page]
class CreatePage(webapp2.RequestHandler):

    def get(self):
        email = 'youremail@gmail.com'
        template_values = {
            'page': "Connex.us",
            'email': email,
        }

        template = JINJA_ENVIRONMENT.get_template('templates/create.html')
        self.response.write(template.render(template_values))
# [END create_page]

# [START view_page]
class ViewPage(webapp2.RequestHandler):

    def get(self):
        email = 'youremail@gmail.com'
        template_values = {
            'page': "Connex.us",
            'email': email,
        }
        url, url_linktext, user = logout_func(self)
        template_values['url'] = url
        template_values['url_linktext'] = url_linktext
        template_values['user'] = user

        template = JINJA_ENVIRONMENT.get_template('templates/view.html')
        self.response.write(template.render(template_values))
# [END view_page]

# [START search_page]
class SearchPage(webapp2.RequestHandler):

    def get(self):
        email = 'youremail@gmail.com'
        template_values = {
            'page': "Connex.us",
            'email': email,
        }

        template = JINJA_ENVIRONMENT.get_template('templates/search.html')
        self.response.write(template.render(template_values))
# [END search_page]

# [START trending_page]
class TrendingPage(webapp2.RequestHandler):

    def get(self):
        email = 'youremail@gmail.com'
        template_values = {
            'page': "Connex.us",
            'email': email,
        }

        template = JINJA_ENVIRONMENT.get_template('templates/trending.html')
        self.response.write(template.render(template_values))
# [END trending_page]

# [START social_page]
class SocialPage(webapp2.RequestHandler):

    def get(self):
        email = 'youremail@gmail.com'
        template_values = {
            'page': "Connex.us",
            'email': email,
        }

        template = JINJA_ENVIRONMENT.get_template('templates/social.html')
        self.response.write(template.render(template_values))
# [END social_page]

# [START app]
app = webapp2.WSGIApplication([
    ('/', SignIn),
    ('/create', CreatePage),
    ('/view', ViewPage),
    ('/search', SearchPage),
    ('/trending', TrendingPage),
    ('/social', SocialPage),
    ('/contact', ContactPage),
], debug=True)
# [END app]
