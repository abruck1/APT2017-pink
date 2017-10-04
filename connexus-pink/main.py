# [START imports]
from connexus.services.create_stream import *
from connexus.services.login import *
from connexus.services.manage import *
from connexus.services.search import *
from connexus.services.social import *
from connexus.services.view_all import *
from connexus.services.view_stream import *
from connexus.services.subscribe_stream import *
from connexus.services.trending import *
from connexus.services.upload import *

# [END imports]

# [START app]
app = webapp2.WSGIApplication([
    ('/', Login),
    ('/create', CreateStream),
    (r'/view/(\d+)', ViewStream),
    ('/view', ViewAll),
    (r'/subscribe/(\d+)', SubscribeStream),
    ('/unsubscribe', UnSubscribeStream),
    ('/delete', DeleteStream),
    (r'/search', Search),
    ('/trending', Trending),
    ('/social', Social),
    ('/manage', Manage),
    ('/upload', UploadImage)
], debug=True)
# [END app]
