import os

import jinja2
import webapp2
print(webapp2.__file__)
# import requests

from connexus.common import *
from connexus.ndb_model import *

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.PackageLoader('connexus', 'templates'),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

DOMAIN_NAME = 'sandbox53d25b427601433298c59606e4d513a8.mailgun.org'
API_KEY = 'key-259539f231fa908edfc7e8e1726fb578'


# [START create_page]
class CreateStream(webapp2.RequestHandler):

    def post(self):

        # todo: make sure we are getting the email address
        user = users.get_current_user().email()
        streamName = self.request.get('streamname')
        subscribers = self.request.get('subs')
        tags = self.request.get('tags')
        coverImage = self.request.get('coverUrl')

        # Create a new Stream entity then redirect to /view the new stream
        newStream = Stream(name=streamName,
                           owner=user,
                           coverImageURL=coverImage,
                           viewCount=0,
                           tags=tags.split(",") ) # todo trim after split

        newStream.put()

        # todo send invite emails
        subscriberArray = subscribers.split(",")

        # def send_simple_message():
        #     print("message sent")
        #     return requests.post(
        #         "https://api.mailgun.net/v3/" + DOMAIN_NAME + "/messages",
        #         auth=("api", API_KEY),
        #         data={"from": "<mailgun@" + DOMAIN_NAME + ">",
        #               "to": ["wey.matt@utexas.edu"],
        #               "subject": "Hello",
        #               "text": "Testing some Mailgun awesomness!"})
        #
        # send_simple_message()

        #for sub in subscriberArray:


        #Redirect to /view for this stream
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
