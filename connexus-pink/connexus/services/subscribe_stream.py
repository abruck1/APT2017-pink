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

        try:
            user = users.get_current_user().email()
        except:
            # todo raise error message to user?
            self.redirect('/view/' + streamid + '?e=1')
            return

        if user is not None:
            stream = ndb.Key(Stream, int(streamid)).get()
            if not StreamSubscriber.query(StreamSubscriber.stream == stream.key).filter(StreamSubscriber.user == user).get():
                newStreamSubscriber = StreamSubscriber(stream=stream.key,
                                                       user=user
                                                       )
                newStreamSubscriber.put()
            else:
                # todo pop up saying subscribed?
                pass            
   
            # subtract 1 due to redirect to view will increase the view by one
            stream.viewCount -= 1
            stream.put()
        else:
            # todo pop up message saying not logged in
            pass

        # Redirect to /view for this stream
        self.redirect('/view/' + streamid )

# [END SubscribeStream]

# [START UnSubscribeStream]
class UnSubscribeStream(webapp2.RequestHandler):
    def post(self):

        url = self.request.referer
        try:
            user = users.get_current_user().email()
        except:
            # todo raise error message to user?
            self.redirect('/')
            return

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
        try:
            user = users.get_current_user().email()
        except:
            # todo raise error message to user?
            self.redirect('/')
            return

        delete_stream_list = self.request.params.getall('delete')

        for stream_id in delete_stream_list:
            
            print("delete_stream={}".format(stream_id))
            if user is not None:
                # delete any subscriber to the stream
                stream = ndb.Key(Stream, int(stream_id))
                sub = StreamSubscriber.query(StreamSubscriber.stream == stream).fetch()
                for s in sub:
                    s.key.delete()

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
