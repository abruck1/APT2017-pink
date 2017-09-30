from google.appengine.api import users
from commonMethods import *
from ndb_model import *

from urlparse import urlparse
import re

import os
import jinja2
import webapp2

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.dirname(__file__)),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)


# [START ViewAll]
class ViewAll(webapp2.RequestHandler):
    def get(self):

        # todo paging, check out .fetch_page
        streams = Stream.query().order(Stream.createDate).fetch(8)

        template_values = {
            'streams': streams,
            'page': 'View',
        }
        url, url_linktext, user = logout_func(self)
        template_values['url'] = url
        template_values['url_linktext'] = url_linktext
        template_values['user'] = user

        template = JINJA_ENVIRONMENT.get_template('templates/viewall.html')
        self.response.write(template.render(template_values))
# [END ViewAll]
