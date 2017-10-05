import os

import jinja2
import webapp2

from connexus.common import *
from connexus.ndb_model import *

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.PackageLoader('connexus', 'templates'),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

# [START create_page]
class CreateStream(webapp2.RequestHandler):

    def post(self):

        # todo: make sure we are getting the email address
        user = users.get_current_user().email()
        stream_name = self.request.get('streamname')
        subscribers = self.request.get('subs')
        subs_msg = self.request.get('subs_msg')
        tags = self.request.get('tags')
        cover_image = self.request.get('coverUrl')

        existing_stream_name = Stream.query(Stream.name == stream_name).get()
        if existing_stream_name:
            print("Stream exists")

            # Redirect to manage page as per spec
            self.redirect('/error')
        else:
            # Create a new Stream entity then redirect to /view the new stream
            new_stream = Stream(name=stream_name,
                                owner=user,
                                coverImageURL=cover_image,
                                viewCount=0,
                                tags=tags.split(",") ) # todo trim after split
            new_stream.put()

            # todo send invite emails
            subscriber_array = subscribers.split(",")
            print("len={0} subscriber_array{1}".format(len(subscriber_array), subscriber_array))
            for subscriber in subscriber_array:
                if subscriber != "":
                    send_simple_message(subscriber, subs_msg, new_stream)

            # Redirect to manage page as per spec
            self.redirect('/manage')
    
    def get(self):
    
        template_values = {
            'page': 'Create',
        }
        url, url_linktext, user = logout_func(self)
        template_values['url'] = url
        template_values['url_linktext'] = url_linktext
        template_values['user'] = user 

        template = JINJA_ENVIRONMENT.get_template('create.html')
        self.response.write(template.render(template_values))

# [END create_page]
