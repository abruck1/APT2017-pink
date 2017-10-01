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
import os, sys

from google.appengine.api import users
#from google.appengine.ext import ndb
import jinja2
import webapp2

from Home import *
from Manage import *
from create_stream import *
from view_stream import *
from view_all import *
from Search import *
from Trending import *
from Social import *
from ndb_model import *
from commonMethods import *

# [END imports]

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.dirname(__file__)),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

# FIXME: REMOVE CONTACT PAGE
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

# [START app]
app = webapp2.WSGIApplication([
    ('/', HomePage),
    ('/create', CreateStream),
    (r'/view/(\d+)', ViewStream),
    ('/view', ViewAll),
    ('/search', SearchPage),
    ('/trending', TrendingPage),
    ('/social', SocialPage),
    ('/contact', ContactPage),
    ('/manage', ManagePage),
], debug=True)
# [END app]
