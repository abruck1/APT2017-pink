from google.appengine.api import users
from commonMethods import *
from ndbClass import *

from urlparse import urlparse
import re

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
            #Check to see if user is present in StreamUser table, if not add them.
            stream_user = StreamUser.query(StreamUser.key == ndb.Key('StreamUser',user.user_id())).get()
            if stream_user == None:
                stream_user = StreamUser(email = user.email(), id=user.user_id())
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
        
        print("view posting")
        
        fileName = self.request.get('file_name')
        comments = self.request.get('comments')
        
        url = self.request.referer
        streamName = re.search(r'\?(.*)', url).group(1)
        print("stream name={0} fileName={1} comments={2}".format(streamName, fileName, comments))
        
        # add the file to the ndb and add it to this stream
            
        #Redirect to /view for this stream
        self.redirect('/viewOne?'+streamName)

    def get(self):
    
        user = users.get_current_user()
        print("getting")
        if user:
            #Check to see if user is present in StreamUser table, if not add them.
            print("StreamUser.key={0} user.user_id={1} ndb.key={2}".format(StreamUser.key, user.user_id(), ndb.Key('StreamUser',user.user_id())))
            stream_user = StreamUser.query(StreamUser.key == ndb.Key('StreamUser',user.user_id())).get()
            if not stream_user:
                stream_user = StreamUser(email = user.email(), id=user.user_id())
                stream_user.put()

        user_stream = Stream.query(Stream.owner == stream_user.key).fetch()
        user_stream = Stream.query()

        #foo = urlparse(self.response)
        foo = self.request.GET #get('name')
        for f in foo:
            print("Stream.key={0} stream_id={1} ndb.key={2}".format(Stream.key, f, ndb.Key('Stream',str(f))))
            #user_stream = Stream.query(Stream.key == ndb.Key('Stream', f)).get()
            #user_stream = Stream.query(Stream.key == ndb.Key('Stream', str(f))).get()
            print("f={0} and f[]={1}".format(Stream.key, f))
            assert(user_stream != None)
            for s in user_stream:
                    if s.name == f:
                        user_stream = s
                        break

        user_stream.numViews = user_stream.numViews + 1
        user_stream.put()

        template_values = {
            'stream': user_stream,
            'page': 'View',
        }
        url, url_linktext, user = logout_func(self)
        template_values['url'] = url
        template_values['url_linktext'] = url_linktext
        template_values['user'] = user

        template = JINJA_ENVIRONMENT.get_template('templates/view.html')
        self.response.write(template.render(template_values))
# [END view_page]
