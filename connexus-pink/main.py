# [START imports]
from connexus.services.create_stream import *
from connexus.services.get_upload_url import *
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
from connexus.services.nearby import *
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
    (r'/autocomplete_search', AutocompleteSearch),
    ('/trending', Trending),
    ('/social', Social),
    ('/error', Error),
    ('/manage', Manage),
    ('/upload', UploadImage),
    ('/trendingcron', TrendingCron),
    ('/getuploadurl', GetUploadURL),

    ('/m_view', MobileViewAll),
    (r'/m_view/(\d+)', MobileViewStream),
    (r'/m_search', MobileSearch),
    ('/m_manage', MobileManage),
    (r'/m_nearby', MobileNearby),

], debug=True)
# [END app]
