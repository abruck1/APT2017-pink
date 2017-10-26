import jinja2
import webapp2
import urllib2
import json
from connexus.common import *
from connexus.ndb_model import *

from geopy.distance import vincenty

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.PackageLoader('connexus', 'templates'),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)


# [START MobileNearby]
class MobileNearby(webapp2.RequestHandler):
    def get(self):
        in_long = self.request.get('long')
        in_lat = self.request.get('lat')
       
        print("passed in long={0} lat{1}".format(in_long, in_lat))

        stream_images = StreamImage.query().fetch()


        if stream_images is None:
            # todo error
            pass

        data = []
        for s in stream_images:
            print("Latitude={}".format(s.latitude))
            print("Longitude={}".format(s.longitude))

            input_coord = (in_lat, in_long)
            db_coord = (s.latitude, s.longitude)
            distance = vincenty(input_coord, db_coord).feet
            print(distance)
            print(s.key.parent().get().name)

            image = {
                'stream_name': s.key.parent().get().name,
                'distance': distance,
                'image_url': get_stream_image_url(s.imageBlobKey),
                'stream_id': s.key.parent().get().key.id(),
            }

            data.append(image)
        
            #dist = gpxpy.geo.haversine_distance(in_long, in_lat, s.longitude, s.latitude)
            #print(dist)

        # # todo this could be none
        # stream_images = StreamImage.cursor_pagination(stream.key, prev_cursor, next_cursor, 16)

        # image_urls = []
        # for stream_image in stream_images['objects']:
        #     image_urls.append(get_stream_image_url(stream_image.imageBlobKey))

        # # generate upload URL
        # # it will work as is, but there's a 10 min timeout on the blob key
        # upload_url = blobstore.create_upload_url('/upload', )


        # # build json for return
        # data = {
        #     'page': 'View',
        #     'stream_id': stream.key,
        #     'stream_name': stream.name,
        #     'image_urls': image_urls,
        #     'upload_url': upload_url,
        #     'error': show_error,
        #     'already_subscribed': show_success,
        #     'prev_cursor': stream_images['prev_cursor'],
        #     'next_cursor': stream_images['next_cursor'],
        #     'prev': stream_images['prev'],
        #     'next': stream_images['next'],
        # }

        self.response.headers['Content-Type'] = 'application/json'
        self.response.write(json.dumps(data, cls=MyJsonEncoder))

# [END MobileNearby]