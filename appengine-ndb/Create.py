from google.appengine.api import users
from commonMethods import *
from ndbClass import *

import os
import jinja2
import webapp2

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.dirname(__file__)),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)


# [START create_page]
class CreatePage(webapp2.RequestHandler):

    def post(self):
        
        user = users.get_current_user()
        
        streamName = self.request.get('streamname')
        subscribers = self.request.get('subs')
        tags = self.request.get('tags')
        coverImage = self.request.get('coverUrl')
        myStreamUser = StreamUser.query(StreamUser.email==user.email()).get()
        
        subscriberArray = subscribers.split(",") 
        tagArray = tags.split(",")
        
        #Create a new Stream entity then redirect to /view the new stream
        newStream = Stream(name=streamName, user=myStreamUser.key, coverImage=coverImage, numViews=0)
        newStream.put()
        
        print "len(subscriberArray) = ", len(subscriberArray)
        
        for sub in subscriberArray:
            print "sub = ", subby
            myUser = StreamUser.query(StreamUser.email == sub).get()
            newSub = StreamSubscriber(stream = newStream.key, user = myUser.key)
            newSub.put()
        
        for tag in tagArray:
            newTag = Tag.get_or_insert(tag)
            newStreamTag = StreamTag(stream = newStream.key, tag = newTag.key)
            newStreamTag.put()
            
        #Redirect to /view for this stream
        self.redirect('/view')
    
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
