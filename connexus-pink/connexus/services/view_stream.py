import jinja2
import json
import webapp2
from connexus.common import *
from connexus.ndb_model import *
from google.appengine.ext import blobstore

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.PackageLoader('connexus', 'templates'),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)


# [START ViewStream]
class ViewStream(webapp2.RequestHandler):
    def get(self, streamid):
        prev_cursor = self.request.get('prev_cursor', '')
        next_cursor = self.request.get('next_cursor', '')

        show_error = self.request.get('e')
        print("show_error={}".format(show_error))
        if show_error == "":
            show_error = 0
        show_success = self.request.get('s')
        print("show_success={}".format(show_success))
        if show_success == "":
            show_success = 0
        
        stream = ndb.Key(Stream, int(streamid)).get()

        if stream is None:
            # todo error
            pass

        # increment view count and add view to trending queue
        if prev_cursor == '' and next_cursor == '':
            stream.viewCount += 1

        stream.put()

        stream_view = StreamView(parent=stream.key)
        stream_view.put()

        # load images, use pagination


        # todo this could be none
        stream_images = StreamImage.cursor_pagination(stream.key, prev_cursor, next_cursor)

        image_urls = []
        for stream_image in stream_images['objects']:
            image_urls.append(get_stream_image_url(stream_image.imageBlobKey))

        # generate upload URL
        # todo this needs to be generated closer to the actual upload, jquery maybe?
        # it will work as is, but there's a 10 min timeout on the blob key
        upload_url = blobstore.create_upload_url('/upload', )

        # build json for return
        data = {}
        data["page"] = 'View'
        data["stream"] = stream
        data["image_urls"] = image_urls
        data["upload_url"] = upload_url
        data["error"] = show_error
        data["already_subscribed"] = show_success
        data["prev_cursor"] = stream_images['prev_cursor']
        data["next_cursor"] = stream_images['next_cursor']
        data["prev"] = stream_images['prev']
        data["next"] = stream_images['next']

        self.response.headers['Content-Type'] = 'application/json'
        self.response.write(json.dumps(data, separators=(',', ':')))

        # template_values = {
        #     'stream': stream,
        #     'image_urls': image_urls,
        #     'upload_url': upload_url,
        #     'page': 'View',
        #     'error': show_error,
        #     'already_subscribed': show_success,
        #     'prev_cursor': stream_images['prev_cursor'],
        #     'next_cursor': stream_images['next_cursor'],
        #     'prev': stream_images['prev'],
        #     'next': stream_images['next'],
        # }
        # url, url_linktext, user = logout_func(self)
        # template_values['url'] = url
        # template_values['url_linktext'] = url_linktext
        # template_values['user'] = user
        #
        # template = JINJA_ENVIRONMENT.get_template('view.html')
        # self.response.write(template.render(template_values))
# [END ViewStream]
