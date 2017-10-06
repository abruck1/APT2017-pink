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

        # load images
        # todo pagination, see https://www.the-swamp.info/blog/pagination-google-app-engine/
        # todo this could be none
        stream_images = StreamImage.query(ancestor=stream.key).order(-StreamImage.createDate).fetch()

        image_urls = []
        for stream_image in stream_images:
            # crop to 32 pixels
            image_urls.append(get_stream_image_url(stream_image.imageBlobKey))

        # generate upload URL
        # todo this needs to be generated closer to the actual upload, jquery maybe?
        # it will work as is, but there's a 10 min timeout on the blob key
        upload_url = blobstore.create_upload_url('/upload', )

        # action = "http://connexus-pink.appspot.com/_ah/upload/AMmfu6Yk3J5eyl1YWQVdyInu7l60ezT-aIv2hdS-XqrOz-NAXv_BqyPX-gDhgbOftfiV5DAkfAgK7Tepo9da6l_TQTQlRT17evnLVb1qlEsH-koKtuiuw3_T9Ai5S1kR1hQfMx8JjJWsXjG2QxyTMTIcaNxbkOOZfrhCGBiPlr1pdxxA6pnAuMQ/ALBNUaYAAAAAWdXWoYBYLP245jkZIhu7vDjIeObHdYdu/?streamid=5649391675244544"

        # action="http://localhost:8080/_ah/upload/aghkZXZ-Tm9uZXIiCxIVX19CbG9iVXBsb2FkU2Vzc2lvbl9fGICAgICA-JUJDA?streamid=5275456790069248" method="post" enctype="multipart/form-data">

        template_values = {
            'stream': stream,
            'image_urls': image_urls,
            'upload_url': upload_url,
            'page': 'View',
            'error': show_error,
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