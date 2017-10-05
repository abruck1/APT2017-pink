import jinja2
from connexus.ndb_model import *
from google.appengine.ext.webapp import blobstore_handlers

JINJA_ENVIRONMENT = jinja2.Environment(
  loader=jinja2.PackageLoader('connexus', 'templates'),
  extensions=['jinja2.ext.autoescape'],
  autoescape=True)

# [START UploadImage]
class UploadImage(blobstore_handlers.BlobstoreUploadHandler):
    def post(self):
        streamid = self.request.get('streamid')
        imagefile = self.get_uploads()[0]

        # todo error handling

        # get the stream, todo test for error
        stream = (ndb.Key('Stream', int(streamid))).get()

        stream_image = StreamImage(
            parent=stream.key,
            imageBlobKey=imagefile.key())

        #store the image
        stream_image.put()

        #update the stream
        stream.lastPicDate = stream_image.createDate
        stream.imgCount += 1
        # need to subtract one due to the redirect increasing viewCount by one
        stream.viewCount -= 1
        stream.put()

        # todo what to do with comments?
        # comments = self.request.get('comments')

        # Redirect to /view for this stream
        self.redirect('/view/' + streamid)
# [END UploadImage]
