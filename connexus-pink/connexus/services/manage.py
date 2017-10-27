import jinja2
import json
import webapp2
from connexus.common import *
from connexus.ndb_model import *

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.PackageLoader('connexus', 'templates'),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)


# [START Manage]
class Manage(webapp2.RequestHandler):
    def get(self):
        try:
            user = users.get_current_user().email()
            user_streams = Stream.query(Stream.owner == user).fetch()
            user_subscriptions = StreamSubscriber.query(StreamSubscriber.user == user).fetch()
        except:
            # todo raise error message to user?
            user_streams = ""
            user_subscriptions = ""

        template_values = {
            'user_streams': user_streams,
            'subscribe': user_subscriptions,
            'page': 'Manage',
        }
        url, url_linktext, user = logout_func(self)
        template_values['url'] = url
        template_values['url_linktext'] = url_linktext
        template_values['user'] = user
        
        template = JINJA_ENVIRONMENT.get_template('manage.html')
        self.response.write(template.render(template_values))
# [END Manage]

# [START MobileManage]
class MobileManage(webapp2.RequestHandler):
    def get(self):
        try:
            #user = users.get_current_user().email()
            user = self.request.get('user_email')
            user_streams = Stream.query(Stream.owner == user).fetch()
            user_subscriptions = StreamSubscriber.query(StreamSubscriber.user == user).fetch()
        except:
            # todo raise error message to user?
            user_streams = ""
            user_subscriptions = ""

        # build json for return
        data = {
            'page': 'Manage',
            'user_streams': user_streams,
            'subscribe': user_subscriptions,
        }
        json_data = [data]

        self.response.headers['Content-Type'] = 'application/json'
        self.response.write(json.dumps(json_data, cls=MyJsonEncoder))

# [END MobileManage]
