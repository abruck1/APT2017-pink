import jinja2
import webapp2
from connexus.common import *

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.PackageLoader('connexus', 'templates'),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)


# [START social_page]
class Social(webapp2.RequestHandler):
    def get(self):
        email = 'youremail@gmail.com'
        template_values = {
            'page': "Connex.us",
            'email': email,
        }
        url, url_linktext, user = logout_func(self)
        template_values['url'] = url
        template_values['url_linktext'] = url_linktext
        template_values['user'] = user

        template = JINJA_ENVIRONMENT.get_template('social.html')
        self.response.write(template.render(template_values))
# [END social_page]
