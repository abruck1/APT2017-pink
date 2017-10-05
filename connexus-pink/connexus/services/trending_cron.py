import webapp2
import jinja2

from connexus.common import *
from connexus.ndb_model import *

JINJA_ENVIRONMENT = jinja2.Environment(
  loader=jinja2.PackageLoader('connexus', 'templates'),
  extensions=['jinja2.ext.autoescape'],
  autoescape=True)


class TrendingCron(webapp2.RequestHandler):

    def get(self):

        self.response.status = 204

        user = users.get_current_user()
        if user:
            email = user.email()
        else:
            email = "Not logged in"

        template_values = {
            'page': "Trending",
            'email': email,
        }

        url, url_linktext, user = logout_func(self)
        template_values['url'] = url
        template_values['url_linktext'] = url_linktext
        template_values['user'] = user

        template = JINJA_ENVIRONMENT.get_template('trending_cron.html')
        self.response.write(template.render(template_values))
