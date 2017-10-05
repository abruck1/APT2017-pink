from google.appengine.api import images
from google.appengine.ext import blobstore
import jinja2
import webapp2

from connexus.common import *
from connexus.ndb_model import *

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.PackageLoader('connexus', 'templates'),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)


# [START ViewStream]
class ViewStream(webapp2.RequestHandler):
    def get(self, streamid):

        user = users.get_current_user()

        stream = ndb.Key(Stream, int(streamid)).get()

        #todo handle this in an if block
        if stream is None:
            # todo error
            pass

        # increment view count and add view to trending queue
        stream.viewCount += 1
        stream.put()

        stream_view = StreamView(parent=stream.key)
        stream_view.put()

        # load images
        # todo pagination, see https://www.the-swamp.info/blog/pagination-google-app-engine/
        # todo this could be none
        stream_images = StreamImage.query(ancestor=stream.key).fetch()

        image_urls = []
        for stream_image in stream_images:
            # todo make sure the append is putting the images in the correct order
            image_urls.append(images.get_serving_url(stream_image.imageBlobKey))

        # generate upload URLd
        # todo this needs to be generated closer to the actual upload, jquery maybe?
        # it will work as is, but there's a 10 min timeout on the blob key
        upload_url = blobstore.create_upload_url('/upload')

        template_values = {
            'stream': stream,
            'image_urls': image_urls,
            'upload_url': upload_url,
            'page': 'View',
        }
        url, url_linktext, user = logout_func(self)
        template_values['url'] = url
        template_values['url_linktext'] = url_linktext
        template_values['user'] = user

        template = JINJA_ENVIRONMENT.get_template('view.html')
        self.response.write(template.render(template_values))

# [END ViewStream]

# from google.appengine.api import images
# avatar = images.resize(avatar, 32, 32)