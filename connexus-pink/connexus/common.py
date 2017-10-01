from google.appengine.api import users

def logout_func(self, user_text='Logout', user_url='/'):
    user = users.get_current_user()
    if user:
        url_linktext = user_text
        url = users.create_logout_url(user_url)
    else:
        url_linktext = 'Login'
        url = users.create_login_url('view')
        user = "Anonymous"
    return (url, url_linktext, user)
