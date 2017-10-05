import httplib2

from urllib import urlencode
from google.appengine.api import users
from google.appengine.api import images

DOMAIN_NAME = 'sandbox53d25b427601433298c59606e4d513a8.mailgun.org'
API_KEY = 'key-259539f231fa908edfc7e8e1726fb578'
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


def send_simple_message(recipient, subs_msg, stream):
    http = httplib2.Http()
    http.add_credentials('api', API_KEY)

    url = 'https://api.mailgun.net/v3/{}/messages'.format(DOMAIN_NAME)
    data = {
        'from': 'Connex.us Pink <mailgun@{}>'.format(DOMAIN_NAME),
        'to': recipient,
        'subject': 'Subscribe to my Connex.us Stream',
        'text': 'Test message from Mailgun',
        'html': '<p>' + subs_msg + '</p><br><a href="connexus-pink.appspot.com/view/{}/"> '
                'Click here to subscribe to the stream </a>'.format(stream.key.id())
    }

    resp, content = http.request(
        url, 'POST', urlencode(data),
        headers={"Content-Type": "application/x-www-form-urlencoded"})

    if resp.status != 200:
        raise RuntimeError(
            'Mailgun API error: {} {}'.format(resp.status, content))


def send_trend_report(recipient, top_three_streams):
    http = httplib2.Http()
    http.add_credentials('api', API_KEY)
    message = "See links below for the top three most popular streams"

    top_three_stream_ids = []
    for stream in top_three_streams:
        top_three_stream_ids.append(stream.key.id())

    url = 'https://api.mailgun.net/v3/{}/messages'.format(DOMAIN_NAME)
    data = {
        'from': 'Connex.us Pink <mailgun@{}>'.format(DOMAIN_NAME),
        'to': recipient,
        'subject': 'Subscribe to my Connex.us Stream',
        'text': 'Test message from Mailgun',
        'html': '<p>' + message + '</p><br><a href="connexus-pink.appspot.com/view/{}/"> '
                'Most Trending Stream </a>'
                '<a href="connexus-pink.appspot.com/view/{}/"> '
                'Second Most Trending Stream </a>'
                '<a href="connexus-pink.appspot.com/view/{}/"> '
                'Third Most Trending Stream </a>'.format(top_three_stream_ids[0],
                                                         top_three_stream_ids[3],
                                                         top_three_stream_ids[2])
    }

    resp, content = http.request(
        url, 'POST', urlencode(data),
        headers={"Content-Type": "application/x-www-form-urlencoded"})

    if resp.status != 200:
        raise RuntimeError(
            'Mailgun API error: {} {}'.format(resp.status, content))

def get_stream_image_url(image_blobkey):
    # returns the image URL with the resizing
    return images.get_serving_url(image_blobkey) + '=s32-c'

def get_cover_image_url():
    # hmm, if it's just a URL, we can't manipulate it as easy as the images in the blobstore
    pass
