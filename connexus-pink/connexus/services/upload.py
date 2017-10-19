import jinja2
import random
from connexus.ndb_model import *
from google.appengine.api import images
from google.appengine.ext.webapp import blobstore_handlers

JINJA_ENVIRONMENT = jinja2.Environment(
  loader=jinja2.PackageLoader('connexus', 'templates'),
  extensions=['jinja2.ext.autoescape'],
  autoescape=True)


# [START UploadImage]
class UploadImage(blobstore_handlers.BlobstoreUploadHandler):
    def post(self):
        streamid = self.request.get('streamid')

        try:
            # retrieve the stream's key for storing with the image, makes the update to the stream faster
            stream_key = ndb.Key(Stream, int(streamid))

            # UploadImage is only called once per upload, don't need to use a for loop
            imagefile = self.get_uploads()[0]

            stream_image = StreamImage(
                parent=stream_key,
                imageBlobKey=imagefile.key())

            stream_image.longitude = random.uniform(-180, 180)
            stream_image.latitude = random.uniform(-85, 85)

            # store the image
            stream_image.put()

            # update the stream here, should be fast
            # get the stream
            stream = stream_key.get()

            # update the stream
            if stream.coverImageURL == "":
                stream.coverImageURL = images.get_serving_url(stream_image.imageBlobKey)

            stream.lastPicDate = stream_image.createDate
            # stream.imageCount = StreamImage.get_image_count(stream_key)

            # need to subtract one due to the redirect increasing viewCount by one
            # shouldn't need to do this with the redirect disabled
            # if stream.viewCount > 0:
            #     stream.viewCount -= 1



            stream.put()

        except:
            self.redirect(str(self.request.referer))
            return

        # self.redirect('/view/' + streamid)
# [END UploadImage]
