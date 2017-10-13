import jinja2
import webapp2
from connexus.common import *
from connexus.ndb_model import *

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.PackageLoader('connexus', 'templates'),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)


# [START Trending]
class Trending(webapp2.RequestHandler):
    def post(self):
        report_setting = self.request.get('reportfreq')
        app_config = AppConfig.query().fetch(1)
        if app_config:
            app_config[0].trendEmailSend = report_setting
            app_config[0].put()
        else:
            new_config = AppConfig(trendEmailSend=report_setting)
            new_config.put()

        self.redirect('/trending')

    def get(self):
        report_setting = AppConfig.query().fetch(1)
        checked = "never"
        if report_setting:
            checked = report_setting[0].trendEmailSend

        labels = ["never", "minutes", "hour", "day"]
        labels_text = ["No Reports", "Every 5 minutes", "Every Hour", "Every Day"]
        # get trends as stream objects to pass to html page
        streams = Stream.query().order(-Stream.viewsInPastHour).fetch(3)

        template_values = {
            'page': "Trending",
            'streams': streams,
            'labels': labels,
            'labels_text': labels_text,
            'property_checked': checked,
        }
        url, url_linktext, user = logout_func(self)
        template_values['url'] = url
        template_values['url_linktext'] = url_linktext
        template_values['user'] = user

        template = JINJA_ENVIRONMENT.get_template('trending.html')
        self.response.write(template.render(template_values))
# [END Trending]
