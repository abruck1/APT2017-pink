import jinja2
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
        show_error = self.request.get('e')
        print("show_error={}".format(show_error))
        if show_error == "":
            show_error = 0

        stream = ndb.Key(Stream, int(streamid)).get()

        if stream is None:
            # todo error
            pass

        # increment view count and add view to trending queue
        stream.viewCount += 1
        stream.put()

        stream_view = StreamView(parent=stream.key)
        stream_view.put()

        # load images, use pagination
        prev_cursor = self.request.get('prev_cursor', '')
        next_cursor = self.request.get('next_cursor', '')

        # todo this could be none
        stream_images = StreamImage.cursor_pagination(stream.key, prev_cursor, next_cursor)

        image_urls = []
        for stream_image in stream_images['objects']:
            image_urls.append(get_stream_image_url(stream_image.imageBlobKey))

        # generate upload URL
        # todo this needs to be generated closer to the actual upload, jquery maybe?
        # it will work as is, but there's a 10 min timeout on the blob key
        upload_url = blobstore.create_upload_url('/upload', )

        template_values = {
            'stream': stream,
            'image_urls': image_urls,
            'upload_url': upload_url,
            'page': 'Connex.us',
            'error': show_error,
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
