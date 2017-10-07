import jinja2
import webapp2
from connexus.common import *

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.PackageLoader('connexus', 'templates'),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

# google auth needs this
# CLIENT_ID = "726264805787-r04pldtjtkr88b255pfu802do7h6b0r0.apps.googleusercontent.com"


# [START Login]
class Login(webapp2.RequestHandler):
    def get(self):
        user = users.get_current_user()

        if user:
            self.redirect("/view")

        template_values = {
            'page': "Connex.us",
        }
        url, url_linktext, user = logout_func(self, user_text='Enter Connex.us!')
        template_values['url'] = url
        template_values['url_linktext'] = url_linktext
        template_values['user'] = user

        template = JINJA_ENVIRONMENT.get_template('login.html')
        self.response.write(template.render(template_values))
# [END Login]
