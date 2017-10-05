import webapp2
import jinja2

from connexus.common import *
from connexus.ndb_model import *

# JINJA_ENVIRONMENT = jinja2.Environment(
#   loader=jinja2.PackageLoader('connexus', 'templates'),
#   extensions=['jinja2.ext.autoescape'],
#   autoescape=True)


class TrendingCron(webapp2.RequestHandler):

    def get(self):

        # user = users.get_current_user()
        # if user:
        #     email = user.email()
        # else:
        #     email = "Not logged in"

        # **************************update past hour views for each stream********************
        # load stream view items and delete all items with timestamp older than 1 hour

        # get id from stream view parent for each item in stream view
        # loop through results and store count for each id in a dictionary with id as key and count as value

        # update stream table for counts in the past hour
        # ************************************************************************************

        # **************************check email report setting and send email if necessary****

        # template_values = {
        #     'page': "Trending",
        #     'email': email,
        # }
        #
        # url, url_linktext, user = logout_func(self)
        # template_values['url'] = url
        # template_values['url_linktext'] = url_linktext
        # template_values['user'] = user
        #
        # template = JINJA_ENVIRONMENT.get_template('trending_cron.html')
        # self.response.write(template.render(template_values))
        self.response.status = 204
