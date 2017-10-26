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

        page = self.request.get('p')
        if page == "":
            page = 1
        else:
            page = int(page)
       
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
       
        data.sort(key=lambda x: x['distance'])

        MAX_DATA_PER_PAGE = 16

        low_range = 0 + MAX_DATA_PER_PAGE*(page-1)
        high_range = MAX_DATA_PER_PAGE + MAX_DATA_PER_PAGE*(page-1)
        
        sliced_data = data[low_range : high_range]

        next_page = 'false'
        if high_range < len(data):
            next_page = 'true'

        if page != 1:
            prev_page = 'true'
        else:
            prev_page = 'false'

        json_array = []

        json_array.append({
            'next_page': next_page,
            'prev_page': prev_page,
            'next_cursor': page+1,
            'prev_cursor': page-1,
            'found_streams': sliced_data,
        })

        self.response.headers['Content-Type'] = 'application/json'
        self.response.write(json.dumps(json_array, cls=MyJsonEncoder))

# [END MobileNearby]