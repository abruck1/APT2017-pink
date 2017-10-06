import os

import jinja2
import webapp2

from connexus.common import *
from connexus.ndb_model import *

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.PackageLoader('connexus', 'templates'),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)


# [START ViewAll]
class ViewAll(webapp2.RequestHandler):
    def get(self):

        # todo paging, check out .fetch_page
        streams = Stream.query().order(Stream.createDate).fetch(8)

        template_values = {
            'streams': streams,
            'page': 'Connex.us',
        }
        url, url_linktext, user = logout_func(self)
        template_values['url'] = url
        template_values['url_linktext'] = url_linktext
        template_values['user'] = user

        template = JINJA_ENVIRONMENT.get_template('viewall.html')
        self.response.write(template.render(template_values))
# [END ViewAll]
