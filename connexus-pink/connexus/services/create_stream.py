import os

import jinja2
import webapp2
import httplib2

from connexus.common import *
from connexus.ndb_model import *
from urllib import urlencode

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

        def send_simple_message(recipient):
            http = httplib2.Http()
            http.add_credentials('api', API_KEY)

            url = 'https://api.mailgun.net/v3/{}/messages'.format(DOMAIN_NAME)
            data = {
                'from': 'Example Sender <mailgun@{}>'.format(DOMAIN_NAME),
                'to': recipient,
                'subject': 'This is an example email from Mailgun',
                'text': 'Test message from Mailgun'
            }

            resp, content = http.request(
                url, 'POST', urlencode(data),
                headers={"Content-Type": "application/x-www-form-urlencoded"})

            if resp.status != 200:
                raise RuntimeError(
                    'Mailgun API error: {} {}'.format(resp.status, content))

        for subscriber in subscriberArray:
            send_simple_message(subscriber)
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
