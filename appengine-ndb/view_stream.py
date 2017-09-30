from google.appengine.api import users
from commonMethods import *
from ndb_model import *

from urlparse import urlparse
import re

import os
import jinja2
import webapp2

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.dirname(__file__)),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)


# [START ViewStream]
class ViewStream(webapp2.RequestHandler):
    def post(self):

        # print("view posting")

        fileName = self.request.get('file_name')
        # todo what to do with comments?
        # comments = self.request.get('comments')

        url = self.request.referer

        # todo get the params and upload the file
        stream_id = re.search(r'\?(.*)', url).group(1)
        print("stream name={0} fileName={1} comments={2}".format(stream_id, fileName))

        # add the file to the ndb and add it to this stream

        # Redirect to /view for this stream
        self.redirect('/view?' + stream_id )

    def get(self):

        user = users.get_current_user()


        user_stream = Stream.query()

        request_string = self.request.GET  # get('streamID')
        print("RESULT = {}".format(self.request.get('streamid')))

        my_streamid = int(self.request.get('streamid'))

        #stream = Stream.get_by_id(my_streamid) # == self.request.get('streamid')).get()
        stream = ndb.Key(Stream, my_streamid).get()

            #print("Stream.key={0} stream_id={1} ndb.key={2}".format(Stream.key, param, ndb.Key('Stream', str(param))))
            # user_stream = Stream.query(Stream.key == ndb.Key('Stream', f)).get()
            # user_stream = Stream.query(Stream.key == ndb.Key('Stream', str(f))).get()
            #print("f={0} and f[]={1}".format(Stream.key, param))

        assert (stream is not None)

        stream.viewCount += 1
        stream.put()

        template_values = {
            'stream': stream,
            'page': 'View',
        }
        url, url_linktext, user = logout_func(self)
        template_values['url'] = url
        template_values['url_linktext'] = url_linktext
        template_values['user'] = user

        template = JINJA_ENVIRONMENT.get_template('templates/view.html')
        self.response.write(template.render(template_values))

# [END view_page]
