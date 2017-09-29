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

# [START viewAll_page]
class ViewAllPage(webapp2.RequestHandler):

    def get(self):
    
        user = users.get_current_user()
        
        if user:
            nickname = user.nickname()
            
            #Check to see if user is present in StreamUser table, if not add them.
            stream_user = StreamUser.query(StreamUser.key == ndb.Key('StreamUser',user.user_id())).get()
            if stream_user == None:
                stream_user = StreamUser(email = user.email(), nickName = nickname, id=user.user_id())
                stream_user.put()

        user_streams = Stream.query(Stream.owner == stream_user.key).fetch()
        if user_streams == None:
            print("streams is empty in viewAll")

        template_values = {
            'streams': user_streams,
            'page': 'View',
        }
        url, url_linktext, user = logout_func(self)
        template_values['url'] = url
        template_values['url_linktext'] = url_linktext
        template_values['user'] = user

        template = JINJA_ENVIRONMENT.get_template('templates/viewAll.html')
        self.response.write(template.render(template_values))
# [END viewAll_page]

# [START view_page]
class ViewPage(webapp2.RequestHandler):
    def post(self):
        
        user = users.get_current_user()
        print("posting")
        
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
        
        for sub in subscriberArray:
            print "sub = ", sub
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
    
        user = users.get_current_user()
        print("getting")
        if user:
            nickname = user.nickname()
            
            #Check to see if user is present in StreamUser table, if not add them.
            print("StreamUser.key={0} user.user_id={1} ndb.key={2}".format(StreamUser.key, user.user_id(), ndb.Key('StreamUser',user.user_id())))
            stream_user = StreamUser.query(StreamUser.key == ndb.Key('StreamUser',user.user_id())).get()
            if not stream_user:
                stream_user = StreamUser(email = user.email(), nickName = nickname, id=user.user_id())
                stream_user.put()

        user_streams = Stream.query(Stream.owner == stream_user.key).fetch()

        template_values = {
            'streams': user_streams,
            'page': 'View',
        }
        url, url_linktext, user = logout_func(self)
        template_values['url'] = url
        template_values['url_linktext'] = url_linktext
        template_values['user'] = user

        template = JINJA_ENVIRONMENT.get_template('templates/view.html')
        self.response.write(template.render(template_values))
# [END view_page]
