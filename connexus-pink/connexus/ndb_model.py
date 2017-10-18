from google.appengine.datastore.datastore_query import Cursor
from google.appengine.ext import ndb

STREAM_IMAGES_PER_PAGE = 3


class Stream(ndb.Model):
    owner = ndb.StringProperty()
    name = ndb.StringProperty()
    createDate = ndb.DateTimeProperty(auto_now_add=True)
    coverImageURL = ndb.StringProperty()
    viewCount = ndb.IntegerProperty()
    # imageCount = ndb.IntegerProperty()
    lastPicDate = ndb.DateTimeProperty()
    tags = ndb.StringProperty(repeated=True)
    viewsInPastHour = ndb.IntegerProperty()
    imageCount = ndb.ComputedProperty(lambda self: StreamImage.get_image_count(self.key))



class StreamSubscriber(ndb.Model):
    stream = ndb.KeyProperty(kind='Stream')
    user = ndb.StringProperty()


# uses parent=stream.key on creation
class StreamImage(ndb.Model):
    imageBlobKey = ndb.BlobKeyProperty()
    createDate = ndb.DateTimeProperty(auto_now_add=True)
    lat = ndb.FloatProperty()
    long = ndb.FloatProperty()

    @classmethod
    def get_image_count(cls, ancestor_key):
        return cls.query(ancestor=ancestor_key).count()


    # adapted from https://github.com/zdenulo/gae-ndb-pagination
    @classmethod
    def cursor_pagination(cls, ancestor_key, prev_cursor_str, next_cursor_str):
        if not prev_cursor_str and not next_cursor_str:
            objects, next_cursor, more = cls.query(ancestor=ancestor_key).order(-cls.createDate).fetch_page(STREAM_IMAGES_PER_PAGE)
            prev_cursor_str = ''
            if next_cursor:
                next_cursor_str = next_cursor.urlsafe()
            else:
                next_cursor_str = ''
            next_ = True if more else False
            prev = False
        elif next_cursor_str:
            cursor = Cursor(urlsafe=next_cursor_str)
            objects, next_cursor, more = cls.query(ancestor=ancestor_key).order(-cls.createDate).fetch_page(STREAM_IMAGES_PER_PAGE, start_cursor=cursor)
            prev_cursor_str = next_cursor_str
            next_cursor_str = next_cursor.urlsafe()
            prev = True
            next_ = True if more else False
        elif prev_cursor_str:
            cursor = Cursor(urlsafe=prev_cursor_str)
            objects, next_cursor, more = cls.query(ancestor=ancestor_key).order(cls.createDate).fetch_page(STREAM_IMAGES_PER_PAGE, start_cursor=cursor)
            objects.reverse()
            next_cursor_str = prev_cursor_str
            prev_cursor_str = next_cursor.urlsafe()
            prev = True if more else False
            next_ = True

        return {'objects': objects, 'next_cursor': next_cursor_str, 'prev_cursor': prev_cursor_str, 'prev': prev,
                'next': next_}


class StreamView(ndb.Model):
    viewDate = ndb.DateTimeProperty(auto_now_add=True)


class AppConfig(ndb.Model):
    trendEmailSend = ndb.StringProperty()  # options are: never, minutes, hour, day
