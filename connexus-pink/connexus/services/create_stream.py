import os

import jinja2
import webapp2
import re

from connexus.common import *
from connexus.ndb_model import *

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.PackageLoader('connexus', 'templates'),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

# [START create_page]
class CreateStream(webapp2.RequestHandler):

    def post(self):
        user = users.get_current_user().email()
        stream_name = self.request.get('streamname').strip()
        subscribers = self.request.get('subs')
        subs_msg = self.request.get('subs_msg')
        tags = self.request.get('tags')
        cover_image = self.request.get('coverUrl')

        existing_stream_name = Stream.query(Stream.name == stream_name.strip()).get()
        if existing_stream_name or stream_name == "":
            # Redirect to manage page as per spec
            if stream_name == "":
                self.redirect('/create' + '?e=2')
            else:
                self.redirect('/create' + '?e=1')
            return
        else:
            # Create a new Stream entity then redirect to /view the new stream
            new_stream = Stream(name=stream_name.strip(),
                                owner=user,
                                coverImageURL=cover_image,
                                viewCount=0,
                                # imageCount=0,
                                tags=tags.strip().replace(" ", "").split(",") )
            new_stream.put()

            # todo send invite emails
            subscriber_array = subscribers.split(",")
            # print("len={0} subscriber_array{1}".format(len(subscriber_array), subscriber_array))
            for subscriber in subscriber_array:
                if subscriber != "":
                    print("Before Email match")
                    if re.match("[^@]+@[^@]+\.[^@]+", subscriber):
                        send_simple_message(subscriber, subs_msg, new_stream)
                    else:
                        self.redirect('/create' + '?e=3')
                        return

            # Redirect to manage page as per spec
            self.redirect('/manage')
    
    def get(self):

        try:
            user = users.get_current_user().email()
        except:
            # todo raise error message to user?
            self.redirect('/')
            return

        show_error = self.request.get('e')
        print("show_error={}".format(show_error))
        if show_error == "":
            show_error = 0

        template_values = {
            'page': 'Create',
            'error': show_error,
        }
        url, url_linktext, user = logout_func(self)
        template_values['url'] = url
        template_values['url_linktext'] = url_linktext
        template_values['user'] = user 

        template = JINJA_ENVIRONMENT.get_template('create.html')
        self.response.write(template.render(template_values))


def send_simple_message(recipient, subs_msg, stream):
    http = httplib2.Http()
    http.add_credentials('api', API_KEY)

    url = 'https://api.mailgun.net/v3/{}/messages'.format(DOMAIN_NAME)
    data = {
        'from': 'Connex.us Pink <mailgun@{}>'.format(DOMAIN_NAME),
        'to': recipient,
        'subject': 'Subscribe to my Connex.us Stream',
        'text': 'Test message from Mailgun',
        'html': '<p>' + subs_msg + '</p><br><a href="connexus-pink.appspot.com/view/{}"> '
                'Click here to subscribe to the stream </a>'.format(stream.key.id())
    }

    resp, content = http.request(
        url, 'POST', urlencode(data),
        headers={"Content-Type": "application/x-www-form-urlencoded"})

    if resp.status != 200:
        raise RuntimeError(
            'Mailgun API error: {} {}'.format(resp.status, content))
# [END create_page]
