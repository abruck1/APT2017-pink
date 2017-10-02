

import jinja2
import webapp2

from connexus.common import *
from connexus.ndb_model import *

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.PackageLoader('connexus', 'templates'),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)


# [START ViewStream]
class ViewStream(webapp2.RequestHandler):
    def get(self, streamid):

        user = users.get_current_user()

        stream = ndb.Key(Stream, int(streamid)).get()

        #todo handle this in an if block
        assert (stream is not None)

        stream.viewCount += 1
        stream.put()

        template_values = {
            'stream': stream,
            'page': 'View',
        }
        url, url_linktext, user = logout_func(self)
        template_values['url'] = url
        template_values['url_linktext'] = url_linktext
        template_values['user'] = user

        template = JINJA_ENVIRONMENT.get_template('view.html')
        self.response.write(template.render(template_values))

# [END view_page]

