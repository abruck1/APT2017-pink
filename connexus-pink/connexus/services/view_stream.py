import jinja2
import json
import webapp2
from connexus.common import *
from connexus.ndb_model import *
from google.appengine.ext import blobstore

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.PackageLoader('connexus', 'templates'),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)


# [START ViewStream]
class ViewStream(webapp2.RequestHandler):
    def get(self, streamid):
        prev_cursor = self.request.get('prev_cursor', '')
        next_cursor = self.request.get('next_cursor', '')

        show_error = self.request.get('e')
        print("show_error={}".format(show_error))
        if show_error == "":
            show_error = 0
        show_success = self.request.get('s')
        print("show_success={}".format(show_success))
        if show_success == "":
            show_success = 0
        
        stream = ndb.Key(Stream, int(streamid)).get()

        if stream is None:
            # todo error
            pass

        # increment view count and add view to trending queue
        if prev_cursor == '' and next_cursor == '':
            stream.viewCount += 1

        stream.put()

        stream_view = StreamView(parent=stream.key)
        stream_view.put()

        # load images, use pagination


        # todo this could be none
        stream_images = StreamImage.cursor_pagination(stream.key, prev_cursor, next_cursor, 3)

        image_urls = []
        for stream_image in stream_images['objects']:
            image_urls.append(get_stream_image_url(stream_image.imageBlobKey))

        # generate upload URL
        # this value is replaced by dropzone code
        upload_url = blobstore.create_upload_url('/upload', )

        template_values = {
            'stream': stream,
            'image_urls': image_urls,
            'upload_url': upload_url,
            'page': 'View',
            'error': show_error,
            'already_subscribed': show_success,
            'prev_cursor': stream_images['prev_cursor'],
            'next_cursor': stream_images['next_cursor'],
            'prev': stream_images['prev'],
            'next': stream_images['next'],
        }
        url, url_linktext, user = logout_func(self)
        template_values['url'] = url
        template_values['url_linktext'] = url_linktext
        template_values['user'] = user
        
        template = JINJA_ENVIRONMENT.get_template('view.html')
        self.response.write(template.render(template_values))
# [END ViewStream]

# [START MobileViewStream]
class MobileViewStream(webapp2.RequestHandler):
    def get(self, streamid):
        prev_cursor = self.request.get('prev_cursor', '')
        next_cursor = self.request.get('next_cursor', '')

        show_error = self.request.get('e')
        print("show_error={}".format(show_error))
        if show_error == "":
            show_error = 0
        show_success = self.request.get('s')
        print("show_success={}".format(show_success))
        if show_success == "":
            show_success = 0
        
        stream = ndb.Key(Stream, int(streamid)).get()

        if stream is None:
            # todo error
            pass

        # increment view count and add view to trending queue
        if prev_cursor == '' and next_cursor == '':
            stream.viewCount += 1

        stream.put()

        stream_view = StreamView(parent=stream.key)
        stream_view.put()

        # load images, use pagination


        # todo this could be none
        stream_images = StreamImage.cursor_pagination(stream.key, prev_cursor, next_cursor, 16)

        image_urls = []
        for stream_image in stream_images['objects']:
            image_urls.append(get_stream_image_url(stream_image.imageBlobKey))

        # generate upload URL
        # it will work as is, but there's a 10 min timeout on the blob key
        upload_url = blobstore.create_upload_url('/upload', )


        # build json for return
        data = {
            'page': 'View',
            'stream_id': stream.key,
            'stream_name': stream.name,
            'image_urls': image_urls,
            'upload_url': upload_url,
            'error': show_error,
            'already_subscribed': show_success,
            'prev_cursor': stream_images['prev_cursor'],
            'next_cursor': stream_images['next_cursor'],
            'prev': stream_images['prev'],
            'next': stream_images['next'],
        }

        json_array = []
        json_array.append(data)
        self.response.headers['Content-Type'] = 'application/json'
        self.response.write(json.dumps(json_array, cls=MyJsonEncoder))

# [END MobileViewStream]

