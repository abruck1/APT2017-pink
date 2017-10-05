from google.appengine.ext import ndb


class Stream(ndb.Model):
    owner = ndb.StringProperty()
    name = ndb.StringProperty()
    createDate = ndb.DateTimeProperty(auto_now_add=True)
    coverImageURL = ndb.StringProperty()
    viewCount = ndb.IntegerProperty()
    imgCount = ndb.IntegerProperty()
    lastPicDate = ndb.DateTimeProperty()
    tags = ndb.StringProperty(repeated=True)
    imageCount = ndb.IntegerProperty()
    viewsInPastHour = ndb.IntegerProperty()

class StreamSubscriber(ndb.Model):
    stream = ndb.KeyProperty(kind='Stream')
    user = ndb.StringProperty()

# uses parent=stream.key on creation
class StreamImage(ndb.Model):
    imageBlobKey = ndb.BlobKeyProperty()
    createDate = ndb.DateTimeProperty(auto_now_add=True)

class StreamView(ndb.Model):
    viewDate = ndb.DateTimeProperty(auto_now_add=True)

class AppConfig(ndb.Model):
    # 0=none, 1=5min, 2=hour, 3=day
    # todo make this an enum?
    trendEmailSend = ndb.IntegerProperty()
