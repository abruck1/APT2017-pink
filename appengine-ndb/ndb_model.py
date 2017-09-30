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
    user = ndb.KeyProperty()

