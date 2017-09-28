import webapp2

from google.appengine.api import users


class SignIn(webapp2.RequestHandler):

    def get(self):
        #client_id = "521418538274-ful52avc9n4dm8isfbv7igrq54k3teh4.apps.googleusercontent.com"

        user = users.get_current_user()
        print("user={}".format(user))
        if user:
            #url = users.create_logout_url(self.request.uri)
            url = "/view"
            url_linktext = 'Enter Connex.us!'
        else:
            url = users.create_login_url(self.request.uri)
            url_linktext = 'Login'
            user = "Anonymous"

        template_values = {
            'page': "Connex.us",
            'user': user,
            'greetings': "Hello!",
            'url': url,
            'url_linktext': url_linktext,
        }

        template = JINJA_ENVIRONMENT.get_template('templates/home.html')
        self.response.write(template.render(template_values))
