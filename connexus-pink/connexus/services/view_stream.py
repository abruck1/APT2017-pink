
from google.appengine.ext import blobstore
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
        if stream is None:
            # todo error
            pass

        stream.viewCount += 1
        stream.put()

        # generate upload URLd
        # todo this needs to be generated closer to the actual upload, jquery maybe?
        # it will work as is, but there's a 10 min timeout on the blob key
        upload_url = blobstore.create_upload_url('/upload')

        template_values = {
            'stream': stream,
            'upload_url': upload_url,
            'page': 'View',
        }
        url, url_linktext, user = logout_func(self)
        template_values['url'] = url
        template_values['url_linktext'] = url_linktext
        template_values['user'] = user

        template = JINJA_ENVIRONMENT.get_template('view.html')
        self.response.write(template.render(template_values))

# [END ViewStream]

