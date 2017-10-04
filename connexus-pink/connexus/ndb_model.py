from google.appengine.ext import ndb


class Stream(ndb.Model):
    owner = ndb.StringProperty()
    name = ndb.StringProperty()
    createDate = ndb.DateTimeProperty(auto_now_add=True)
    coverImageURL = ndb.StringProperty()
    viewCount = ndb.IntegerProperty()
    lastPicDate = ndb.DateTimeProperty()
    tags = ndb.StringProperty(repeated=True)
    images = ndb.StringProperty(repeated=True)

class StreamSubscriber(ndb.Model):
    stream = ndb.KeyProperty(kind='Stream')
    user = ndb.StringProperty()

# not entirely sure about this class.  also, blob has builtin creation date, may not need the date field
class StreamImage(ndb.Model):
    stream = ndb.KeyProperty(kind='Stream')
    imageBlobKey = ndb.BlobKeyProperty()
    createDate = ndb.DateTimeProperty(auto_now_add=True)