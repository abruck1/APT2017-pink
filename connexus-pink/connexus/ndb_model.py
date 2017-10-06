from google.appengine.ext import ndb


class Stream(ndb.Model):
    owner = ndb.StringProperty()
    name = ndb.StringProperty()
    createDate = ndb.DateTimeProperty(auto_now_add=True)
    coverImageURL = ndb.StringProperty()
    viewCount = ndb.IntegerProperty()
    imageCount = ndb.IntegerProperty()
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
    trendEmailSend = ndb.StringProperty()  # options are: never, minutes, hour, day
