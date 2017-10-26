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
       
        stream_images = StreamImage.query().fetch()

        if stream_images is None:
            self.response.headers['Content-Type'] = 'application/json'
            self.response.write(json.dumps([], cls=MyJsonEncoder))
            return

        data = []
        for s in stream_images:
            input_coord = (in_lat, in_long)
            if s.longitude=="" or s.latitude=="":
                continue

            db_coord = (s.latitude, s.longitude)
            distance = vincenty(input_coord, db_coord).feet

            if s.key.parent().get() is None:
                continue

            image = {
                'stream_name': s.key.parent().get().name,
                'distance': distance,
                'image_url': get_stream_image_url(s.imageBlobKey),
                'stream_id': s.key.parent().get().key.id(),
            }

            data.append(image)
        
        self.response.headers['Content-Type'] = 'application/json'
        self.response.write(json.dumps(data, cls=MyJsonEncoder))

# [END MobileNearby]