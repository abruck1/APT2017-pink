from google.appengine.api import users


# TODO: see if there is a way to log in/out using google auth without
# signing out the google user everywhere in the browser
def logout_func(self, user_text='Logout', user_url='/'):
    user = users.get_current_user()
    if user:
        url_linktext = user_text
        url = users.create_logout_url(user_url)
        user = user.email()
    else:
        url_linktext = 'Login'
        url = users.create_login_url('view')
    return (url, url_linktext, user)
