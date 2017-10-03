import jinja2
import webapp2
import os
from google.appengine.api import app_identity
from google.cloud import storage



# def doUpload(self, streamid):
#   bucket_name = os.environ.get('BUCKET_NAME',
#                                app_identity.get_default_gcs_bucket_name())
#
#   self.response.headers['Content-Type'] = 'text/plain'
#   self.response.write('Demo GCS Application running from Version: '
#                       + os.environ['CURRENT_VERSION_ID'] + '\n')
#   self.response.write('Using bucket name: ' + bucket_name + '\n\n')

JINJA_ENVIRONMENT = jinja2.Environment(
  loader=jinja2.PackageLoader('connexus', 'templates'),
  extensions=['jinja2.ext.autoescape'],
  autoescape=True)

# [START UploadImage]
class UploadImage(webapp2.RequestHandler):
    def post(self, streamid):
        imagefile = self.request.POST['image']

        client = storage.Client()
        default_bucket = 'connexus-pink.appspot.com'#app_identity.get_default_gcs_bucket_name()
        gcs_bucket = client.get_bucket(default_bucket)

        # print("source: {}\ndest: {}\{}".format(file.filename, gcs_bucket.name, file.filename))

        blob = gcs_bucket.blob(imagefile.filename)
        blob.upload_from_string(imagefile.read(), content_type=imagefile.content_type)

        url = blob.public_url

        #print(url)

        # todo what to do with comments?
        # comments = self.request.get('comments')

        # add the file to the ndb and add it to this stream
        # /Users/taylor/Documents/school/apt/pics/utaustin_colored

        # Redirect to /view for this stream
        self.redirect('/view/' + streamid)
# [END UploadImage]

#     def upload_image_file(file):
#         """
#         Upload the user-uploaded file to Google Cloud Storage and retrieve its
#         publicly-accessible URL.
#         """
#         if not file:
#             return None
#
#         public_url = storage.upload_file(
#             file.read(),
#             file.filename,
#             file.content_type
#         )
#
#         current_app.logger.info(
#             "Uploaded file %s as %s.", file.filename, public_url)
#
#         return public_url
#
#     def upload_file(file_stream, filename, content_type):
#         """
#         Uploads a file to a given Cloud Storage bucket and returns the public url
#         to the new object.
#         """
#
#         client = _get_storage_client()
#         bucket = client.bucket(current_app.config['CLOUD_STORAGE_BUCKET'])
#         blob = bucket.blob(filename)
#
#         blob.upload_from_string(
#             file_stream,
#             content_type=content_type)
#
#         url = blob.public_url
#
#         if isinstance(url, six.binary_type):
#             url = url.decode('utf-8')
#
#         return url
#
# # gs://bucket_name/desired_object_name
# #
# # getDefaultGoogleStorageBucketName
# #
# # $default_bucket = CloudStorageTools::getDefaultGoogleStorageBucketName();
# # $fp = fopen("gs://${default_bucket}/hello_default_stream.txt", 'w');
# # fwrite($fp, $newFileContent);
# # fclose($fp);
#
#dev_appserver.py . --default_gcs_bucket_name [BUCKET_NAME]