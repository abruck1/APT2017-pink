# [START imports]
from connexus.services.create_stream import *
from connexus.services.error import *
from connexus.services.login import *
from connexus.services.manage import *
from connexus.services.search import *
from connexus.services.social import *
from connexus.services.subscribe_stream import *
from connexus.services.trending import *
from connexus.services.trending_cron import *
from connexus.services.upload import *
from connexus.services.view_all import *
from connexus.services.view_stream import *
from connexus.services.geo_view import *
# [END imports]


# [START app]
app = webapp2.WSGIApplication([
    ('/', Login),
    ('/create', CreateStream),
    (r'/view/(\d+)/geoview', GeoView),
    (r'/view/(\d+)', ViewStream),
    ('/view', ViewAll),
    (r'/subscribe/(\d+)', SubscribeStream),
    ('/unsubscribe', UnSubscribeStream),
    ('/delete', DeleteStream),
    (r'/search', Search),
    ('/trending', Trending),
    ('/social', Social),
    ('/error', Error),
    ('/manage', Manage),
    ('/upload', UploadImage),
    ('/trendingcron', TrendingCron)
], debug=True)
# [END app]
