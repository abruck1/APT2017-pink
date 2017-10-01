import os
import re

import jinja2
import webapp2

from connexus.common import *
from connexus.ndb_model import *

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.PackageLoader('connexus', 'templates'),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)


# [START SubscribeStream]
class SubscribeStream(webapp2.RequestHandler):
    def post(self, streamid):

        url = self.request.referer
        stream_id = re.search(r'view/(.*)', url).group(1)
        print("stream name={0}".format(stream_id))

        user = users.get_current_user().email()

        if user is not None:
            stream = ndb.Key(Stream, int(streamid)).get()
            newStreamSubscriber = StreamSubscriber(stream=stream.key,
                                                   user=user
                                                   )
            newStreamSubscriber.put()
            # todo pop up saying subscribed?
        else:
            # todo pop up message saying not logged in
            pass

        # Redirect to /view for this stream
        self.redirect('/view/' + stream_id )

# [END vSubscribeStream]

