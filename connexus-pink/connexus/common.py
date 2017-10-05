from google.appengine.api import users
from google.appengine.api import images

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

def get_stream_image_url(image_blobkey):
    # returns the image URL with the resizing
    return images.get_serving_url(image_blobkey) + '=s256'

def get_cover_image_url():
    # hmm, if it's just a URL, we can't manipulate it as easy as the images in the blobstore
    pass
