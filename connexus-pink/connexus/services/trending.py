import os

import jinja2
import webapp2
from connexus.ndb_model import *

from connexus.common import *

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.PackageLoader('connexus', 'templates'),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

# [START trending_page]
class Trending(webapp2.RequestHandler):

    def get(self):

        try:
            user = users.get_current_user().email()
        except:
            # todo raise error message to user?
            self.redirect('/')
            return

        # testing the streamview logic
        streamviews = StreamView.query().fetch()
        trends = {}

        for view in streamviews:
            if view.key.parent().id() not in trends:
                trends[view.key.parent().id()] = 1
            else:
                trends[view.key.parent().id()] += 1

        for stream, view_count in trends.items():
            print(stream, view_count)




        email = 'youremail@gmail.com'
        template_values = {
            'page': "Trending",
            'email': email,
        }
        url, url_linktext, user = logout_func(self)
        template_values['url'] = url
        template_values['url_linktext'] = url_linktext
        template_values['user'] = user

        template = JINJA_ENVIRONMENT.get_template('trending.html')
        self.response.write(template.render(template_values))

# [END trending_page]
