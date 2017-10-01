import os

import jinja2
import webapp2

from connexus.common import *
from connexus.ndb_model import *

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.PackageLoader('connexus', 'templates'),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

# [START manage_page]
class Manage(webapp2.RequestHandler):

    def get(self):
    
        user = users.get_current_user()
        
        if user:
            #Check to see if user is present in StreamUser table, if not add them.
            stream_user = StreamUser.query(StreamUser.key == ndb.Key('StreamUser',user.user_id())).get()
            if not stream_user:
                stream_user = StreamUser(email = user.email(), id=user.user_id())
                stream_user.put()

        user_streams = Stream.query(Stream.owner == stream_user.key).fetch()
        user_subscriptions = StreamSubscriber.query(StreamSubscriber.user == stream_user.key).fetch()

        for s in user_subscriptions:
            print("stream.user.email={0}".format(s.user.get().email))

        template_values = {
            'stream': user_streams,
            'subscribe': user_subscriptions,
            'page': 'Manage',
        }
        url, url_linktext, user = logout_func(self)
        template_values['url'] = url
        template_values['url_linktext'] = url_linktext
        template_values['user'] = user 

        template = JINJA_ENVIRONMENT.get_template('manage.html')
        self.response.write(template.render(template_values))

# [END manage_page]
