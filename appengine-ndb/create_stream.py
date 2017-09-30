from google.appengine.api import users
from commonMethods import *
from ndb_model import *

import os
import jinja2
import webapp2

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.dirname(__file__)),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)


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

        #for sub in subscriberArray:


        #Redirect to /view for this stream
        print('CREATE REDIRECT = {}'.format(str(newStream.key.id())))
        self.redirect('/view?streamid=' + str(newStream.key.id()))
    
    def get(self):
    
        template_values = {
            'page': 'Create',
        }
        url, url_linktext, user = logout_func(self)
        template_values['url'] = url
        template_values['url_linktext'] = url_linktext
        template_values['user'] = user 

        template = JINJA_ENVIRONMENT.get_template('templates/create.html')
        self.response.write(template.render(template_values))

# [END create_page]
