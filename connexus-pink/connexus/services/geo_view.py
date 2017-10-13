import jinja2
import random
import webapp2
from connexus.common import *
from connexus.ndb_model import *
from google.appengine.ext import blobstore

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.PackageLoader('connexus', 'templates'),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

maps_key = 'AIzaSyBK8sbPvE14mSYYebJDKofJ4Edw-fBIzq4'


# [START ViewStream]
class GeoView(webapp2.RequestHandler):
    def get(self, streamid):
        stream = ndb.Key(Stream, int(streamid)).get()

        if stream is None:
            # todo error
            return

        # todo this could be none
        stream_images = StreamImage.query(ancestor=stream.key).order(StreamImage.createDate).fetch()

        image_urls = []
        image_create_date = []
        image_longitude = []
        image_lat = []
        number_of_images = 0
        for stream_image in stream_images:
            image_urls.append(get_stream_image_url(stream_image.imageBlobKey))
            image_create_date.append(str(stream_image.createDate.date()))
            image_longitude.append(random.uniform(-180, 180))
            image_lat.append(random.uniform(-85, 85))
            number_of_images += 1

        print("dates {}".format(image_create_date))

        # generate upload URL
        # todo this needs to be generated closer to the actual upload, jquery maybe?
        # it will work as is, but there's a 10 min timeout on the blob key
        upload_url = blobstore.create_upload_url('/upload', )

        template_values = {
            'stream': stream,
            'image_urls': image_urls,
            'image_create_date': image_create_date,
            'image_longitude': image_longitude,
            'image_lat': image_lat,
            'page': 'Connex.us',
            'number_of_images': number_of_images,
            'maps_key': maps_key,
        }
        url, url_linktext, user = logout_func(self)
        template_values['url'] = url
        template_values['url_linktext'] = url_linktext
        template_values['user'] = user

        template = JINJA_ENVIRONMENT.get_template('geoview.html')
        self.response.write(template.render(template_values))
# [END ViewStream]
