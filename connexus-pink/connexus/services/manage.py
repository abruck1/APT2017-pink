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
    
        user = users.get_current_user().email()
        
        user_streams = Stream.query(Stream.owner == user).fetch()
        user_subscriptions = StreamSubscriber.query(StreamSubscriber.user == user).fetch()

        print("USER_STREAMS={0} USER_SUBSCRIPTIONS={1}".format(user_streams, user_subscriptions))

        for s in user_subscriptions:
            print("stream.user={0}".format(s.user))

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
