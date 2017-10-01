import os

import jinja2
import webapp2
from google.appengine.api import search

import urllib2

from connexus.common import *

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.PackageLoader('connexus', 'templates'),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

# [START search_page]
class Search(webapp2.RequestHandler):

    def post(self):
        search_string = self.request.get('search_string')


        self.redirect('/search?' + search_string)


    def get(self):
        search_string = urllib2.unquote(self.request.query_string)

        index = search.Index(name='connexus_search')

        num_results = 0
        template_values = {
            'page': "Search",
            'search_string': search_string,
            'num_results': num_results,
        }
        url, url_linktext, user = logout_func(self)
        template_values['url'] = url
        template_values['url_linktext'] = url_linktext
        template_values['user'] = user
       
        template = JINJA_ENVIRONMENT.get_template('search.html')
        self.response.write(template.render(template_values))

# [END search_page]
