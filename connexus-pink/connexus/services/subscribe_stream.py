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

# [END SubscribeStream]

# [START UnSubscribeStream]
class UnSubscribeStream(webapp2.RequestHandler):
    def post(self):

        url = self.request.referer
        user = users.get_current_user().email()

        subscriber_stream_list = self.request.params.getall('unsub')

        for sub_stream_id in subscriber_stream_list:
            
            print("unsub_stream={}".format(sub_stream_id))
            if user is not None:
                stream_sub = ndb.Key(StreamSubscriber, int(sub_stream_id))
                sub = StreamSubscriber.query(StreamSubscriber.key == stream_sub).filter(StreamSubscriber.user == user).get()
                sub.key.delete()
                # todo pop up saying unsubscribed?
            else:
                # todo pop up message saying not logged in
                pass

        # Redirect to /view for this stream
        self.redirect('/manage')

# [END UnSubscribeStream]

# [START DeleteStream]
class DeleteStream(webapp2.RequestHandler):
    def post(self):

        url = self.request.referer
        user = users.get_current_user().email()

        delete_stream_list = self.request.params.getall('delete')

        for stream_id in delete_stream_list:
            
            print("delete_stream={}".format(stream_id))
            if user is not None:
                stream = ndb.Key(Stream, int(stream_id))
                stream_to_be_del = Stream.query(Stream.key == stream).filter(Stream.owner == user).get()
                stream_to_be_del.key.delete()
                # todo pop up saying unsubscribed?
            else:
                # todo pop up message saying not logged in
                pass

        # Redirect to /view for this stream
        self.redirect('/manage')

# [END DeleteStream]